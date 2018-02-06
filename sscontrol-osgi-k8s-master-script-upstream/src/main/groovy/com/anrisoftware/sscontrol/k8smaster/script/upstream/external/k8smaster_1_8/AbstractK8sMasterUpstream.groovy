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
package com.anrisoftware.sscontrol.k8smaster.script.upstream.external.k8smaster_1_8

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUpstreamLinux
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sMasterUpstream extends AbstractK8sUpstreamLinux {

    def setupClusterDefaults() {
        K8sMaster service = service
        assertThat("cluster advertise address=null", service.cluster.advertiseAddress, not(isEmptyOrNullString()))
        super.setupClusterDefaults()
    }

    def setupApiServersDefaults() {
        log.debug 'Setup api-servers hosts defaults for {}', service
        K8sMaster service = service
        if (service.cluster.apiServers.size() == 0) {
            service.cluster.apiServers << defaultApiServerHost
        }
    }

    def setupBindDefaults() {
        log.debug 'Setup bind defaults for {}', service
        K8sMaster service = service
        if (!service.binding.insecureAddress) {
            service.binding.insecureAddress = defaultInsecureAddress
        }
        if (!service.binding.secureAddress) {
            service.binding.secureAddress = defaultSecureAddress
        }
        if (!service.binding.port) {
            service.binding.port = defaultPort
        }
        if (!service.binding.insecurePort) {
            service.binding.insecurePort = defaultInsecurePort
        }
    }

    String getDefaultApiServerHost() {
        getScriptProperty "default_api_server_host"
    }

    @Override
    def getLog() {
        log
    }
}
