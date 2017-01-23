/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.etcd.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.etcd.external.Etcd
import com.anrisoftware.sscontrol.etcd.external.Binding.BindingFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_1_Upstream_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    BindingFactory bindingFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Etcd_3_1_Upstream_Systemd_Templates')
        this.servicesTemplate = templates.getResource('etcd_services')
        this.configsTemplate = templates.getResource('etcd_configs')
    }

    def setupDefaults() {
        Etcd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug 'debug', level: defaultDebugLogLevel
        }
        if (service.bindings.size() == 0) {
            service.bindings << bindingFactory.create()
        }
        if (!service.bindings[0].address) {
            service.bindings[0].address = defaultBindingAddress
        }
        if (!service.bindings[0].scheme) {
            service.bindings[0].scheme = defaultBindingScheme
        }
        if (!service.bindings[0].port) {
            service.bindings[0].port = defaultBindingPort
        }
        if (service.advertises.size() == 0) {
            service.advertises << bindingFactory.create()
        }
        if (!service.advertises[0].address) {
            service.advertises[0].address = defaultAdvertiseAddress
        }
        if (!service.advertises[0].scheme) {
            service.advertises[0].scheme = defaultAdvertiseScheme
        }
        if (!service.advertises[0].port) {
            service.advertises[0].port = defaultAdvertisePort
        }
        if (!service.memberName) {
            service.memberName = defaultMemberName
        }
    }

    def createDirectories() {
        log.info 'Create etcd directories.'
        def dir = systemdSystemDir
        def rundir = runDir
        shell privileged: true, """
mkdir -p '$dir'
mkdir -p '$rundir'
useradd -r $user
chown $user '$rundir'
""" call()
    }

    def createServices() {
        log.info 'Create etcd services.'
        def dir = systemdSystemDir
        [
            [
                resource: servicesTemplate,
                name: 'etcdService',
                privileged: true,
                override: false,
                dest: "$dir/etcd.service",
                vars: [:],
            ],
        ].each { template it call() }
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def createConfig() {
        log.info 'Create etcd configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: configsTemplate,
                name: 'etcdConfig',
                privileged: true,
                override: false,
                dest: "$configDir/etcd.conf",
                vars: [:],
            ],
        ].each { template it call() }
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getDataDir() {
        properties.getFileProperty "data_dir", base, defaultProperties
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    def getDefaultDebugLogLevel() {
        properties.getNumberProperty 'default_debug_log_level', defaultProperties intValue()
    }

    def getDefaultBindingAddress() {
        properties.getProperty 'default_binding_address', defaultProperties
    }

    def getDefaultBindingScheme() {
        properties.getProperty 'default_binding_scheme', defaultProperties
    }

    def getDefaultBindingPort() {
        properties.getNumberProperty 'default_binding_port', defaultProperties intValue()
    }

    def getDefaultAdvertiseAddress() {
        properties.getProperty 'default_advertise_address', defaultProperties
    }

    def getDefaultAdvertiseScheme() {
        properties.getProperty 'default_advertise_scheme', defaultProperties
    }

    def getDefaultAdvertisePort() {
        properties.getNumberProperty 'default_advertise_port', defaultProperties intValue()
    }

    def getDefaultMemberName() {
        properties.getProperty 'default_member_name', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
