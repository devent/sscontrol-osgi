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
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
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
        def sysdir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        def datadir = runDir
        def libexecdir = libexecDir
        shell privileged: true, """
mkdir -p '$sysdir'
mkdir -p '$tmpdir'
mkdir -p '$datadir'
mkdir -p '$libexecdir'
""" call()
    }

    def createServices() {
        log.info 'Create Flannel-Docker services.'
        def sysdir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        def libexecdir = libexecDir
        def dockerdir = dockerServiceDir
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
        shell privileged: true, """
systemctl daemon-reload
""" call()
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

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", base, defaultProperties
    }

    File getLibexecDir() {
        properties.getFileProperty "libexec_dir", base, defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getDockerNetworkFile() {
        properties.getFileProperty "docker_network_file", dockerNetworkDir, defaultProperties
    }

    File getDockerNetworkDir() {
        properties.getFileProperty "docker_network_dir", base, defaultProperties
    }

    File getDockerServiceDir() {
        properties.getFileProperty "docker_service_dir", base, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
