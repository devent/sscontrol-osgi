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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8.KubectlClusterLinux
import com.anrisoftware.sscontrol.k8snode.script.upstream.external.k8snode_1_8.K8sNodeUpstreamSystemd

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Node service from the upstream sources for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sNodeUpstreamDebian extends K8sNodeUpstreamSystemd {

    @Inject
    K8sNodeDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
    }

    def setupDefaults() {
        setupMiscDefaults()
        setupClusterDefaults()
        setupClusterHostDefaults()
        setupClusterApiDefaults()
        setupKubeletDefaults()
        setupPluginsDefaults()
    }

    def createService() {
        createDirectories()
        uploadK8sCertificates()
        uploadEtcdCertificates()
        createKubeletService()
        createKubeletConfig()
        createKubeletKubeconfig()
        createKubeletManifests()
        createHostRkt()
        createFlannelCni()
        createWorkerKubeconfig()
    }

    def postInstall() {
        applyTaints()
        applyLabels()
    }

    @Inject
    void setKubectlClusterLinuxFactory(KubectlClusterDebianFactory factory) {
        this.kubectlClusterLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
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
    KubectlClusterLinux getKubectlCluster() {
        kubectlClusterLinux
    }
}
