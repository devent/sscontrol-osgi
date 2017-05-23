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
package com.anrisoftware.sscontrol.icinga.icinga2.script.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.service.external.Icinga
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures Icinga 2 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Icinga_2 extends ScriptBase {

    TemplateResource mirrorTemplate

    TemplateResource dockerdTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Icingace_17_Systemd_Templates')
        this.dockerdTemplate = templates.getResource('dockerd_config')
        this.mirrorTemplate = templates.getResource('mirror_config')
    }

    def setupDefaults() {
        Icinga service = service
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

    def createDirectories() {
        log.info 'Create docker directories.'
        def dropin = systemdDropinDir
        shell privileged: true, """
mkdir -p '$dropin'
""" call()
    }

    def createIcingadConfig() {
        Icinga service = service
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
        Icinga service = service
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
        Icinga service = service
        if (service.registry.mirrorHosts.size() == 0) {
            return
        }
        service.registry.mirrorHosts.each {
            def dir = getCertsDir(it)
            Tls tls = it.tls
            if (tls.ca) {
                log.info 'Deploy mirror certificates for {}.', it
                shell privileged: true, "mkdir -p '$dir'" call()
                copyResource privileged: true, src: tls.ca, dest: "$dir/${tls.caName}" call()
            }
        }
    }

    def stopServices() {
        stopSystemdService 'docker'
    }

    def startServices() {
        if (upgradeKernel) {
            if (check("uname -a | grep '$kernelFullVersion'")) {
                log.debug 'Kernel is upgraded, starting docker.'
                startEnableSystemdService 'docker'
            } else {
                log.debug 'Kernel is upgraded but not used, not starting docker.'
                enableSystemdService 'docker'
                shell privileged: true, """
umount $dockerAufsDirectory; true
rm -rf $dockerAufsDirectory; true
""" call()
            }
        } else {
            startEnableSystemdService 'docker'
        }
    }

    File getSystemdDropinDir() {
        properties.getFileProperty 'systemd_dropin_dir', base, defaultProperties
    }

    List getIcingaVariables() {
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

    File getIcingaAufsDirectory() {
        getFileProperty 'docker_aufs_directory', base, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
