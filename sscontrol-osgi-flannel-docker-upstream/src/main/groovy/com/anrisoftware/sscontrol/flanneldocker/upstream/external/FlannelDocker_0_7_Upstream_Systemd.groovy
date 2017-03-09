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
package com.anrisoftware.sscontrol.flanneldocker.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.flanneldocker.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.groovy.external.NumberTrueRenderer

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.7 service from the upstream
 * sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class FlannelDocker_0_7_Upstream_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attrs = [renderers: [new NumberTrueRenderer()]]
        def templates = templatesFactory.create('FlannelDocker_0_7_Upstream_Systemd_Templates', attrs)
        this.servicesTemplate = templates.getResource('flannel_services')
        this.configsTemplate = templates.getResource('flannel_configs')
    }

    def createDirectories() {
        log.info 'Create Flannel-Docker directories.'
        def certsdir = certsDir
        shell privileged: true, """
mkdir -p '$systemdSystemDir'
mkdir -p '$systemdTmpfilesDir'
mkdir -p '$runDir'
mkdir -p '$libexecDir'
mkdir -p '$dockerServiceDropDir'
mkdir -p '$certsdir'
chown root.root '$certsdir'
chmod o-rx '$certsdir'
""" call()
    }

    def uploadEtcdCerts() {
        log.info 'Uploads etcd certificates.'
        FlannelDocker service = service
        if (!service.etcd) {
            return
        }
        Tls tls = service.etcd.tls
        def certsdir = certsDir
        [
            [
                name: "ca",
                src: tls.ca,
                dest: "$certsdir/$tls.caName",
            ],
            [
                name: "cert",
                src: tls.cert,
                dest: "$certsdir/$tls.certName",
            ],
            [
                name: "key",
                src: tls.key,
                dest: "$certsdir/$tls.keyName",
            ],
        ].each {
            if (it.src) {
                copyResource it call()
            }
        }
    }

    def createServices() {
        log.info 'Create Flannel-Docker services.'
        def sysdir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        def libexecdir = libexecDir
        def dockerdir = dockerServiceDropDir
        [
            [
                resource: servicesTemplate,
                name: 'flannelService',
                privileged: true,
                dest: "$sysdir/flanneld.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'flannelTmpfiles',
                privileged: true,
                dest: "$tmpdir/flannel.conf",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'flannelDockerConfig',
                privileged: true,
                dest: "$dockerdir/flannel.conf",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'dockerNetworkConfig',
                privileged: true,
                override: false,
                dest: "$dockerNetworkFile",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createConfig() {
        log.info 'Create Flannel-Docker configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: configsTemplate,
                name: 'flannelConfig',
                privileged: true,
                dest: "$configDir/flanneld",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def patchDockerService() {
        replace dest: dockerServiceFile, privileged: true with {
            line 's|ExecStart=/usr/bin/dockerd.*|ExecStart=/usr/bin/dockerd -H fd:// $DOCKER_NETWORK_OPTIONS|'
            it
        }.call()
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", base, defaultProperties
    }

    File getLibexecDir() {
        properties.getFileProperty "libexec_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getCertsDir() {
        properties.getFileProperty "certs_dir", base, defaultProperties
    }

    File getDockerNetworkFile() {
        properties.getFileProperty "docker_network_file", dockerNetworkDir, defaultProperties
    }

    File getDockerNetworkDir() {
        properties.getFileProperty "docker_network_dir", base, defaultProperties
    }

    File getDockerServiceDropDir() {
        properties.getFileProperty "docker_service_drop_dir", base, defaultProperties
    }

    File getDockerServiceDir() {
        properties.getFileProperty "docker_service_dir", base, defaultProperties
    }

    File getDockerServiceFile() {
        properties.getFileProperty "docker_service_file", dockerServiceDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
