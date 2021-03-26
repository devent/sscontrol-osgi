/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_13.debian_9

import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8sbase.script.upstream.debian.external.k8s_1_13.debian.debian_9.AbstractK8sUpstreamDebian
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_13.AbstractKubectlLinux

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master service from the upstream sources Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sNodeUpstreamDebian extends AbstractK8sUpstreamDebian {

    @Inject
    K8sNodeDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
    }

    def setupDefaults() {
        setupMiscDefaults()
        setupLabelsDefaults()
        setupApiServersDefaults()
        setupClusterDefaults()
        setupClusterHostDefaults()
        setupKubeletDefaults()
        setupPluginsDefaults()
        setupKernelParameter()
    }

    def createService() {
        createDirectories()
        uploadEtcdCertificates()
        createKubeletConfig()
        restartKubelet()
    }

    def postInstall() {
        applyLabels()
        applyTaints()
        installPlugins()
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
    AbstractKubectlLinux getKubectlCluster() {
        kubectlClusterLinux
    }
}
