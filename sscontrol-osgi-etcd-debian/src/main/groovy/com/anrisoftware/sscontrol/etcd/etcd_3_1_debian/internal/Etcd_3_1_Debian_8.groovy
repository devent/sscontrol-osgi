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
package com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal

import static com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal.Etcd_3_1_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Etcd_3_1_Debian_8 extends ScriptBase {

    @Inject
    Etcd_3_1_Debian_8_Properties debianPropertiesProvider

    @Inject
    Etcd_3_1_Systemd_Debian_8_Factory systemdFactory

    @Inject
    Etcd_3_1_Upstream_Debian_8_Factory upstreamFactory

    @Inject
    Etcd_3_1_Upstream_Systemd_Debian_8_Factory upstreamSystemdFactory

    @Override
    def run() {
        installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamSystemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
    }

    void installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, "apt-get -y install ${packages.join(' ')}" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
