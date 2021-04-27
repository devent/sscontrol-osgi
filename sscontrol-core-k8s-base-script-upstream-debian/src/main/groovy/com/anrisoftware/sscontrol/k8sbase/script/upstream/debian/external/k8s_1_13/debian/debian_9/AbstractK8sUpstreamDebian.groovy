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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.debian.external.k8s_1_13.debian.debian_9

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_13.linux.AbstractK8sUpstreamLinux
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_10_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the K8s service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sUpstreamDebian extends AbstractK8sUpstreamLinux {

    DebianUtils debian

    @Inject
    void setDebian(Debian_10_UtilsFactory factory) {
        this.debian = factory.create this
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
cat <<EOF >${aptKubernetesListFile}
deb http://apt.kubernetes.io/ kubernetes-xenial main
EOF
""" call()
        debian.installPackages packages: kubeadmPackages, update: true
    }

    def installPlugins() {
        K8s service = service
        if (service.plugins.containsKey("nfs-client")) {
            installNfsClinetPlugin()
        }
    }

    def installNfsClinetPlugin() {
        debian.installPackages getScriptListProperty("nfs_client_packages")
    }

    @Override
    def getLog() {
        log
    }
}
