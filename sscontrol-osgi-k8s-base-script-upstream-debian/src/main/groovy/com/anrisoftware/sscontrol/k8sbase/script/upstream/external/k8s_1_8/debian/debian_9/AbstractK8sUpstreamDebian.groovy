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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.debian.debian_9

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUpstreamLinux

import groovy.util.logging.Slf4j

/**
 * Configures the K8s service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sUpstreamDebian extends AbstractK8sUpstreamLinux {

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

    @Override
    def getLog() {
        log
    }
}
