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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux
import com.anrisoftware.sscontrol.k8smaster.script.upstream.external.k8smaster_1_8.AbstractK8sMasterUpstream
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master service from the upstream sources Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterUpstreamDebian extends AbstractK8sMasterUpstream {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
    }

    def setupDefaults() {
        setupMiscDefaults()
        setupApiServersDefaults()
        setupClusterDefaults()
        setupClusterHostDefaults()
        setupClusterApiDefaults()
        setupBindDefaults()
        setupKubeletDefaults()
        setupPluginsDefaults()
        setupKernelParameter()
    }

    def createService() {
        createDirectories()
        uploadK8sCertificates()
        uploadEtcdCertificates()
        createKubeadmConfig()
        createKubeletConfig()
        restartKubelet()
    }

    /**
     * Restarts kubelet after the configuration was deployed.
     */
    def restartKubelet() {
        shell privileged: true, timeout: timeoutShort, """
systemctl daemon-reload
systemctl restart kubelet
systemctl status kubelet
""" call()
    }

    def installKubeadm() {
        shell privileged: true, timeout: timeoutLong, """
apt-get update && apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb http://apt.kubernetes.io/ kubernetes-xenial main
EOF
apt-get update
apt-get install -y ${kubeadmPackages.join(" ")}
""" call()
    }

    def installKube() {
        shell privileged: true, timeout: timeoutLong, """
if ! kubeadm token list; then
kubeadm init --config /root/kubeadm.yaml ${ignoreChecksErrors}
fi
""" call()
    }

    def setupKubectl() {
        shell """
mkdir -p \$HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf \$HOME/.kube/config
sudo chown \$(id -u):\$(id -g) \$HOME/.kube/config
""" call()
    }

    def getIgnoreChecksErrors() {
        List ignoreCheckErrors = []
        if (!failSwapOn) {
            ignoreCheckErrors << "Swap"
        }
        if (ignoreCheckErrors.size() > 0) {
            "--ignore-preflight-errors " + ignoreCheckErrors.join(",")
        } else {
            ""
        }
    }

    def postInstall() {
        //applyTaints()
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
    AbstractKubectlLinux getKubectlCluster() {
        kubectlClusterLinux
    }

    /**
     * Returns the needed packages for kubeadm.
     *
     * <ul>
     * <li>profile property {@code kubeadm_packages}</li>
     * </ul>
     */
    List getKubeadmPackages() {
        getScriptListProperty "kubeadm_packages"
    }
}
