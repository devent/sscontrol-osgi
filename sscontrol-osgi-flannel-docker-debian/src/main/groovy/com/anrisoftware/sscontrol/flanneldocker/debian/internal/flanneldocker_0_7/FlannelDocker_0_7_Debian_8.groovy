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
package com.anrisoftware.sscontrol.flanneldocker.debian.internal.flanneldocker_0_7

import static com.anrisoftware.sscontrol.flanneldocker.debian.internal.flanneldocker_0_7.FlannelDocker_0_7_Debian_8_Service.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.7 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDocker_0_7_Debian_8 extends ScriptBase {

    @Inject
    FlannelDocker_0_7_Debian_8_Properties debianPropertiesProvider

    @Inject
    FlannelDocker_0_7_Systemd_Debian_8_Factory systemdFactory

    @Inject
    FlannelDocker_0_7_Upstream_Debian_8_Factory upstreamFactory

    @Inject
    FlannelDocker_0_7_Upstream_Systemd_Debian_8_Factory upstreamSystemdFactory

    @Override
    def run() {
        installPackages()
        setupDefaults()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamSystemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
    }

    def setupDefaults() {
        log.info 'Setup Flannel-Docker defaults.'
        FlannelDocker service = this.service
        assertThat "etcd.address=null", service.etcd.address, is(notNullValue())
        if (!service.debugLogging.modules['debug']) {
            service.debug 'debug', level: defaultDebugLogLevel
        }
        if (!service.etcd.prefix) {
            service.etcd.prefix = defaultEtcdPrefix
        }
        if (!service.backend) {
            service.backend defaultFlannelBackendType
        }
        if (!service.network.address) {
            service.network.address = defaultFlannelNetworkAddress
        }
    }

    void installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, "apt-get -y install ${packages.join(' ')}" with { //
            sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
    }

    def getDefaultDebugLogLevel() {
        properties.getNumberProperty 'default_debug_log_level', defaultProperties intValue()
    }

    def getDefaultEtcdPrefix() {
        properties.getProperty 'default_etcd_prefix', defaultProperties
    }

    def getDefaultFlannelBackendType() {
        properties.getProperty 'default_flannel_backend_type', defaultProperties
    }

    def getDefaultFlannelNetworkAddress() {
        properties.getProperty 'default_flannel_network_address', defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
