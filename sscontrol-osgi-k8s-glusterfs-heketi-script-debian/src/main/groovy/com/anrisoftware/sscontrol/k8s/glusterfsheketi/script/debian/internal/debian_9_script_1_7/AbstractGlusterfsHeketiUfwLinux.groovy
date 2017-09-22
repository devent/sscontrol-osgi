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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.debian_9_script_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.GlusterfsHeketi
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
abstract class AbstractGlusterfsHeketiUfwLinux extends ScriptBase {

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        if (!ufwAvailable) {
            return
        }
        updateFirewall()
    }

    /**
     * Checks that UFW client is available.
     */
    boolean isUfwAvailable() {
        if (!ufw.ufwActive) {
            log.debug 'No Ufw available, nothing to do.'
            return false
        } else {
            return true
        }
    }

    /**
     * Updates the firewall.
     */
    def updateFirewall() {
        GlusterfsHeketi service = this.service
        shell privileged: true, st: """
<vars.nodes:{p|ufw allow from <p> to any port 8472 proto udp};separator="\\n">
""", vars: [nodes: nodesAddresses] call()
    }

    List getNodesAddresses() {
        GlusterfsHeketi service = this.service
        nodesFactory.create(service, scriptsRepository, this).nodes
    }

    @Override
    def getLog() {
        log
    }
}
