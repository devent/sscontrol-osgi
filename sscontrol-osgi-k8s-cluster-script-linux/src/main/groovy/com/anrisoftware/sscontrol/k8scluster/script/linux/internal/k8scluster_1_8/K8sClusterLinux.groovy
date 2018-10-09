package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterScript

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Cluster</i> service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sClusterLinux extends ScriptBase implements K8sClusterScript {

    @Inject
    K8sClusterLinuxProperties linuxPropertiesProvider

    KubectlClusterLinux kubectlClusterLinux

    @Override
    def run() {
        kubectlClusterLinux.run()
    }

    @Inject
    void setKubectlClusterLinuxFactory(KubectlClusterLinuxFactory factory) {
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
    def getLog() {
        log
    }
}
