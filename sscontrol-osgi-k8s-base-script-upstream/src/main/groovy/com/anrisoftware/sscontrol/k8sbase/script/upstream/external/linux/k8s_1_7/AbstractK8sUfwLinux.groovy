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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.linux.k8s_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.types.host.external.TargetHost
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
abstract class AbstractK8sUfwLinux extends ScriptBase {

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
    }

    boolean isUfwAvailable() {
        if (!ufw.ufwActive) {
            log.debug 'No Ufw available.'
            return false
        } else {
            return true
        }
    }

    def updateFirewall() {
        K8s service = this.service
        shell privileged: true, st: """
ufw allow from <podNetwork> to <address>
""", vars: [podNetwork: service.cluster.podRange, address: advertiseAddress] call()
    }

    String getAdvertiseAddress() {
        K8s service = this.service
        def advertiseAddress = service.cluster.advertiseAddress
        if (advertiseAddress instanceof TargetHost) {
            return advertiseAddress.hostAddress
        } else {
            return advertiseAddress.toString()
        }
    }

    @Override
    def getLog() {
        log
    }
}
