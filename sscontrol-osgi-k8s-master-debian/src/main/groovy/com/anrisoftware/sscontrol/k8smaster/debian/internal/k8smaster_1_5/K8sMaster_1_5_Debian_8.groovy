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
package com.anrisoftware.sscontrol.k8smaster.debian.internal.k8smaster_1_5

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMaster_1_5_Debian_8 extends ScriptBase {

    @Inject
    K8sMaster_1_5_Debian_8_Properties debianPropertiesProvider

    @Inject
    Kubectl_1_5_Upstream_Debian_8_Factory upstreamFactory

    @Inject
    K8sMaster_1_5_Upstream_Systemd_Debian_8_Factory upstreamSystemdFactory

    @Override
    def run() {
        installAptPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamSystemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
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
