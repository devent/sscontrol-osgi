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
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterScript

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Cluster</i> 1.5 service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sCluster_1_5_Linux extends ScriptBase implements K8sClusterScript {

    @Inject
    K8sCluster_1_5_Linux_Properties linuxPropertiesProvider

    Kubectl_1_6_Cluster_Linux kubectlClusterLinux

    Kubectl_1_5_Upstream_Linux kubectlUpstreamLinux

    @Override
    def run() {
        checkAptPackages() ? false : installAptPackages()
        kubectlClusterLinux.run()
        kubectlUpstreamLinux.run()
    }

    @Inject
    void setKubectlUpstreamLinuxFactory(Kubectl_1_5_Upstream_Linux_Factory factory) {
        this.kubectlUpstreamLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setKubectlClusterLinuxFactory(Kubectl_1_6_Cluster_Linux_Factory factory) {
        this.kubectlClusterLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    @Override
    void runKubectl(Map<String, Object> vars) {
        kubectlClusterLinux.runKubectl vars
    }

    @Override
    void uploadCertificates(Map<String, Object> vars) {
        kubectlClusterLinux.uploadCertificates(vars)
    }

    @Override
    def getLog() {
        log
    }
}
