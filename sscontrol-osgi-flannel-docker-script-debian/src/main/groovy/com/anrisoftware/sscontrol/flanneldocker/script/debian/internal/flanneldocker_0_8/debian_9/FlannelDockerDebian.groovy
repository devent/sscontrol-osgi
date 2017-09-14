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
package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_8.debian_9

import static com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_8.debian_9.FlannelDockerDebianService.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.8 service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerDebian extends ScriptBase {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    @Inject
    FlannelDockerUpstreamDebianFactory upstreamFactory

    @Inject
    FlannelDockerUpstreamSystemdDebianFactory upstreamSystemdFactory

    @Inject
    FlannelDockerUfwFactory ufwFactory

    IperfConnectionCheck iperf

    DebianUtils debian

    SystemdUtils systemd

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Inject
    void setIperfConnectionCheckFactory(IperfConnectionCheckFactory factory) {
        this.iperf = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        systemd.stopServices()
        debian.installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamSystemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemd.startServices()
        def iperfServers = []
        try {
            iperfServers = iperf.startServers()
            iperf.startClients iperfServers
        } finally {
            if (!iperfServers.empty) {
                iperf.stopServers(iperfServers)
            }
        }
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
