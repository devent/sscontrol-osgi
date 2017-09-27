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
package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.ssh.external.TargetsAddressListFactory
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Configures the Ufw firewall.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerUfw extends ScriptBase {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    @Inject
    TargetsAddressListFactory addressesFactory

    @Inject
    TargetsListFactory nodesFactory

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        FlannelDocker service = this.service
        if (!ufw.ufwActive) {
            log.debug 'No Ufw available.'
            return
        }
        if (service.nodes.empty) {
            log.debug 'No nodes available.'
            return
        }
        updateFirewall()
    }

    def updateFirewall() {
        FlannelDocker service = this.service
        ufw.allowPortsOnNodes nodes, nodesAddresses, ["8472"], "udp", this
    }

    List getNodes() {
        FlannelDocker service = this.service
        nodesFactory.create(service, scriptsRepository, "nodes", this).nodes
    }

    List getNodesAddresses() {
        FlannelDocker service = this.service
        addressesFactory.create(service, scriptsRepository, "nodes", this).nodes
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
