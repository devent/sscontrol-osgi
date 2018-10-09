package com.anrisoftware.sscontrol.k8sbase.script.upstream.debian.external.k8s_1_8.debian.debian_9

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUpstreamLinux
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

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
    void setDebian(Debian_9_UtilsFactory factory) {
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

    @Override
    def getLog() {
        log
    }
}
