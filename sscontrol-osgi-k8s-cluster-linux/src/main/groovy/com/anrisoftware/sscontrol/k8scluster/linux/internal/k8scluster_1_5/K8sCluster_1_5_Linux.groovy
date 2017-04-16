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
package com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Cluster</i> 1.5 service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sCluster_1_5_Linux extends ScriptBase {

    @Inject
    K8sCluster_1_5_Linux_Properties linuxPropertiesProvider

    K8sCluster_1_5_Upstream_Linux upstreamLinux

    @Override
    def run() {
        installAptPackages()
        upstreamLinux.run()
    }

    @Inject
    def injectupStreamLinuxFactory(K8sCluster_1_5_Upstream_Linux_Factory upstreamLinuxFactory) {
        this.upstreamLinux = upstreamLinuxFactory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    def runKubectl(Map vars) {
        upstreamLinux.runKubectl vars
    }

    @Override
    def getLog() {
        log
    }
}