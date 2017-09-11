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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8snode_1_7

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_7.KubectlClusterLinux
import com.anrisoftware.sscontrol.k8snode.script.upstream.external.k8s_1_7.K8sNodeUpstreamSystemd

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Node 1.5 service from the upstream sources
 * for Systemd and Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sNodeUpstreamSystemdDebian extends K8sNodeUpstreamSystemd {

    @Inject
    K8sNodeDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
        stopServices()
        setupMiscDefaults()
        setupClusterDefaults()
        setupClusterApiDefaults()
        setupKubeletDefaults()
        setupPluginsDefaults()
        createDirectories()
        uploadK8sCertificates()
        uploadEtcdCertificates()
        createKubeletService()
        createKubeletConfig()
        createKubeletManifests()
        createHostRkt()
        createFlannelCni()
        createWorkerKubeconfig()
        startServices()
        applyTaints()
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
