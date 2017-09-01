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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7

import static com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7.K8sMasterDebianService.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_7.KubectlClusterLinux
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster
import com.anrisoftware.sscontrol.k8smaster.upstream.script.external.k8s_1_7.K8sMasterUpstreamSystemd

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master service from the upstream sources
 * for Systemd and Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterUpstreamSystemdDebian extends K8sMasterUpstreamSystemd {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
        stopServices()
        setupMiscDefaults()
        setupApiServersDefaults()
        setupAdmissionsDefaults()
        setupAccountDefaults()
        setupClusterDefaults()
        setupClusterApiDefaults()
        setupBindDefaults()
        setupKubeletDefaults()
        setupAuthenticationsDefaults()
        setupPluginsDefaults()
        setupKernelParameter()
        createDirectories()
        uploadK8sCertificates()
        uploadAccountCertificates()
        uploadAuthenticationsCertificates()
        uploadEtcdCertificates()
        createKubeletService()
        createKubeletConfig()
        createKubeletManifests()
        createHostRkt()
        createFlannelCni()
        startServices()
        startAddons()
        startCalico()
        applyTaints()
        applyLabels()
    }

    def setupClusterDefaults() {
        super.setupClusterDefaults()
        K8sMaster service = service
        if (!service.cluster.name) {
            service.cluster.name = 'master'
        }
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
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }

    @Override
    KubectlClusterLinux getKubectlCluster() {
        kubectlClusterLinux
    }
}
