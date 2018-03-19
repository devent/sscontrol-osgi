/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.docker.script.systemd.external

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.docker.service.external.Docker
import com.anrisoftware.sscontrol.docker.service.external.Mirror
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the Docker CE 17 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Dockerce_17_Systemd extends ScriptBase {

    TemplateResource mirrorTemplate

    TemplateResource dockerdTemplate

    TemplateResource daemonTemplate

    SystemdUtils systemd

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, LoggingDriverOptsRenderer loggingDriverOptsRenderer) {
        def a = [renderers:[loggingDriverOptsRenderer]]
        def templates = templatesFactory.create('Dockerce_17_Systemd_Templates', a)
        this.dockerdTemplate = templates.getResource('dockerd_config')
        this.mirrorTemplate = templates.getResource('mirror_config')
        this.daemonTemplate = templates.getResource('daemon_config')
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    def setupDefaults() {
        Docker service = service
        service.registry.mirrorHosts.each {
            def address = it.host
            def host = address.host ? address.host : address.path
            def proto = address.scheme ? address.scheme : getDefaultMirrorProtocol(it.tls)
            def port = address.port != -1 ? address.port : defaultMirrorPort
            it.host = new URI("$proto://$host:$port")
            Tls tls = it.tls
            if (!tls.caName) {
                tls.caName = defaultRegistryMirrorCaName
            }
        }
    }

    def setupDefaultLogDriver() {
        Docker service = service
        if (service.loggingDriver.driver != null) {
            return
        }
        def vars = [:]
        vars.driver = scriptProperties.default_logging_driver_driver
        vars << getScriptMapProperty("default_logging_driver_opts")
        service.log vars
    }

    def createDirectories() {
        log.info 'Create docker directories.'
        def dropin = systemdDropinDir
        shell privileged: true, """
mkdir -p '$dropin'
""" call()
    }

    def createDockerdConfig() {
        Docker service = service
        log.info 'Create dockerd options drop-in.'
        def dropin = systemdDropinDir
        template resource: dockerdTemplate,
        name: 'dockerdConfig',
        privileged: true,
        dest: "$dropin/00_dockerd_opts.conf",
        vars: [:] call()
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def createRegistryMirrorConfig() {
        Docker service = service
        if (service.registry.mirrorHosts.size() == 0) {
            return
        }
        log.info 'Create docker registry mirror configuration.'
        def dropin = systemdDropinDir
        template resource: mirrorTemplate,
        name: 'mirrorService',
        privileged: true,
        dest: "$dropin/10_mirror.conf",
        vars: [:] call()
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def deployMirrorCerts() {
        Docker service = service
        if (service.registry.mirrorHosts.size() == 0) {
            return
        }
        service.registry.mirrorHosts.each {
            def dir = getCertsDir(it)
            Tls tls = it.tls
            if (tls.ca) {
                log.info 'Deploy mirror certificates for {}.', it
                shell privileged: true, "mkdir -p '$dir'" call()
                copyResource privileged: true, src: tls.ca, dest: "$dir/${tls.caName}"
            }
        }
    }

    /**
     * Creates the docker daemon configuration in "/etc/docker/daemon.json".
     */
    def createDaemonConfig() {
        Docker service = service
        boolean notEmpty = false
        notEmpty = notEmpty || haveNativeCgroupdriver
        notEmpty = notEmpty || haveLoggingDriver
        if (!notEmpty) {
            return
        }
        def vars = [:]
        vars << execOptsVars
        vars << [loggingDriver: service.loggingDriver]
        template resource: daemonTemplate,
        name: 'daemonConfig',
        privileged: true,
        dest: "$daemonConfigFile",
        vars: vars call()
    }

    Map getExecOptsVars() {
        def execOpts = []
        haveNativeCgroupdriver ? execOpts << "native.cgroupdriver=${nativeCgroupdriver}" : false
        [execOpts: execOpts.empty ? null : execOpts]
    }

    boolean getHaveLoggingDriver() {
        Docker service = service
        service.loggingDriver.driver != null
    }

    def stopServices() {
        systemd.stopService 'docker'
    }

    def startServices() {
        if (!upgradeKernel) {
            log.debug 'Enabling docker.'
            systemd.startService 'docker'
            systemd.enableService 'docker'
            return
        }
        if (check("uname -a | grep '$kernelFullVersion'")) {
            log.debug 'Kernel is upgraded, starting docker.'
            systemd.startService 'docker'
            systemd.enableService 'docker'
        } else {
            log.debug 'Kernel is upgraded but not used, not starting docker.'
            systemd.enableService 'docker'
            shell privileged: true, """
umount $dockerAufsDirectory; true
rm -rf $dockerAufsDirectory; true
""" call()
        }
    }


    File getSystemdDropinDir() {
        properties.getFileProperty 'systemd_dropin_dir', base, defaultProperties
    }

    File getCertsDir(Mirror mirror) {
        def template = properties.getProperty 'certs_dir_template', defaultProperties
        def host = mirror.host.host != null ? mirror.host.host : mirror.host.path
        def port = mirror.host.port
        def path = new ST(template).add('host', "$host:$port").render()
        base != null ? new File(base, path) : new File(path)
    }

    List getDockerVariables() {
        properties.getListProperty 'docker_variables', defaultProperties
    }

    def getDefaultRegistryMirrorCaName() {
        properties.getProperty 'default_registry_mirror_ca_name', defaultProperties
    }

    def getDefaultMirrorProtocol(Tls tls) {
        if (tls.ca) {
            properties.getProperty 'default_mirror_https_protocol', defaultProperties
        } else {
            properties.getProperty 'default_mirror_http_protocol', defaultProperties
        }
    }

    def getDefaultMirrorPort() {
        properties.getProperty 'default_mirror_port', defaultProperties
    }

    boolean getUpgradeKernel() {
        properties.getBooleanProperty 'upgrade_kernel', defaultProperties
    }

    String getKernelFullVersion() {
        properties.getProperty 'kernel_full_version', defaultProperties
    }

    File getDockerAufsDirectory() {
        getFileProperty 'docker_aufs_directory'
    }

    String getNativeCgroupdriver() {
        getScriptProperty 'native_cgroupdriver'
    }

    boolean getHaveNativeCgroupdriver() {
        !nativeCgroupdriver.empty
    }

    File getDaemonConfigFile() {
        getScriptFileProperty 'daemon_config_file'
    }

    @Override
    def getLog() {
        log
    }
}
