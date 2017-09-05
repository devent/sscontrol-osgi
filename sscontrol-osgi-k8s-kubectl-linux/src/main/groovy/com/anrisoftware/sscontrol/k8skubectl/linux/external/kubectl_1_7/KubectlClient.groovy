package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j

/**
 * Kubectl client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubectlClient {

    HostServiceScript script

    ClusterHost cluster

    @Inject
    KubectlClient(@Assisted HostServiceScript script, @Assisted ClusterHost cluster) {
        this.script = script
        this.cluster = cluster
    }

    /**
     * Waits until the node is ready.
     *
     * @param vars
     * <ul>
     * <li>kubeconfigFile: the path of the kubeconfig file on the server.
     * <li>cluster: the ClusterHost.
     * <li>args: kubectl arguments.
     * </ul>
     */
    def waitNodeReady(String node) {
        log.info 'Wait for node to be ready: {}', node
    }
}
