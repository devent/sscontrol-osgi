package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux

import groovy.util.logging.Slf4j

/**
 * Checks the pod connectivity between nodes with iperf3.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sNodeCheckPodConnectivityIperfLinux extends ScriptBase {

    TemplateResource iperfTemplate

    @Override
    Object run() {
        if (!checkPodConnectivity) {
            return
        }
        def pods = createServer()
        getServersAddresses(pods).eachWithIndex { address, int i ->
        }
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8s_1_8_K8sNodeCheckPodConnectivityIperfLinuxTemplates')
        this.iperfTemplate = templates.getResource('iperf_ds_yaml')
    }

    /**
     * Creates the iperf3 server on each node.
     *
     * @return {@link List} of the created iperf3 pods.
     */
    List createServer() {
        log.info 'Create iperf3 pods.'
        def kubectl = kubectlCluster
        def iperf = [
            image: scriptProperties.pod_connectivity_image_name,
            version: scriptProperties.pod_connectivity_image_version,
            limits: [cpu: "50m", memory: "50Mi"],
            requests: [cpu: "50m", memory: "50Mi"],
        ]
        withRemoteTempFile { File tmp ->
            template resource: iperfTemplate,
            name: 'iperf-ds-yaml', dest: tmp, vars: [iperf: iperf] call()
            kubectl.runKubectl args: ["apply", "-f", tmp]
        }
    }

    /**
     * Returns the addresses of the created iperf3 servers on each node.
     *
     * @return {@link List} of the pod IP addresses.
     */
    List getServersAddresses(List pods) {
        def kubectl = kubectlCluster
    }

    /**
     * Returns the run kubectl for the cluster.
     */
    abstract AbstractKubectlLinux getKubectlCluster()

    /**
     * Returns true if we need to check the pod connectivity between nodes.
     */
    boolean getCheckPodConnectivity() {
        getScriptBooleanProperty 'check_pod_connectivity'
    }
}
