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
package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j

/**
 * Provides kubectl for GNU/Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractKubectlLinux extends ScriptBase {

    @Inject
    TlsFactory tlsFactory

    TemplateResource kubectlTemplate

    TemplateResource kubeconfTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attr = [renderers: [new UriBase64Renderer()]]
        def templates = templatesFactory.create('Kubectl_1_8_Linux_Templates', attr)
        this.kubectlTemplate = templates.getResource('kubectl_cmd')
        this.kubeconfTemplate = templates.getResource('kubectl_conf')
    }

    /**
     * Returns the {@link ClusterHost} from where to run kubectl from to
     * access the cluster.
     */
    List<ClusterHost> getCluster() {
        K8s service = service
        service.clusterHosts
    }

    /**
     * Creates and uploads kubeconfig for the cluster.
     *
     * @param vars
     * <li>kubeconfigFile: the path of the kubeconfig file on the server.
     */
    def uploadKubeconfig(Map vars) {
        log.info 'Uploads kubeconfig for {}.', vars
        assertThat "kubeconfigFile!=null", vars.kubeconfigFile, notNullValue()
        cluster.each {
            Credentials c = it.credentials
            Map v = new HashMap(vars)
            v.vars = new HashMap(vars)
            v.vars.certs = c.hasProperty('tls') ? certsData(c.tls) : [:]
            v.resource = kubeconfTemplate
            v.name = 'kubectlConf'
            v.dest = vars.kubeconfigFile
            template v call()
        }
    }

    /**
     * Runs kubectl with a specific kube-config file.
     *
     * @param vars
     * <ul>
     * <li>kubeconfigFile: the path of the kube-config file on the server.
     * <li>args: kubectl arguments.
     * </ul>
     */
    def runKubectlKubeconfig(Map vars) {
        log.info 'Run kubectl with {}', vars
        assertThat "kubeconfigFile!=null", vars.kubeconfigFile, notNullValue()
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.resource = kubectlTemplate
        v.name = 'kubectlKubeconfigCmd'
        shell v call()
    }

    /**
     * Runs kubectl.
     *
     * @param vars
     * <ul>
     * <li>args: kubectl arguments.
     * </ul>
     */
    def runKubectl(Map vars) {
        log.info 'Run kubectl with {}', vars
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.vars.service = service
        v.resource = kubectlTemplate
        v.name = 'kubectlCmd'
        runShellOnHosts v
    }

    /**
     * Waits for the node to be available.
     *
     * @param vars additional arguments to the shell, like the timeout.
     * @param node the node name.
     */
    def waitNodeAvailable(Map vars, String node) {
        log.info 'Wait for node to be available: {}', node
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.vars.node = node
        v.resource = kubectlTemplate
        v.name = 'waitNodeAvailableCmd'
        runShellOnHosts v
    }

    /**
     * Waits for the node to be ready.
     *
     * @param vars additional arguments to the shell, like the timeout.
     * @param node the node name.
     */
    def waitNodeReady(Map vars, String node) {
        log.info 'Wait for node to become ready: {}', node
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.vars.node = node
        v.resource = kubectlTemplate
        v.name = 'waitNodeReadyCmd'
        runShellOnHosts v
    }

    /**
     * Applies the taints on the node.
     *
     * @param vars additional arguments to the shell, like the timeout.
     * @param node the node name.
     * @param taints the taints to apply.
     */
    def applyNodeTaints(Map vars, String node, Map taints) {
        log.debug 'Apply taints {} for node {} with {}', taints, node, vars
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.vars.node = node
        v.vars.taints = taints
        v.resource = kubectlTemplate
        v.name = 'applyTaintsCmd'
        runShellOnHosts v
    }

    /**
     * Applies the label on the node.
     *
     * @param vars additional arguments to the shell, like the timeout.
     * @param node the node name.
     * @param labels the labels to apply.
     */
    def applyNodeLabels(Map vars, String node, def labels) {
        log.debug 'Apply labels {} for node {} with {}', labels, node, vars
        Map v = new HashMap(vars)
        v.vars = new HashMap(vars)
        v.vars.node = node
        v.vars.labels = labels
        v.resource = kubectlTemplate
        v.name = 'applyLabelsCmd'
        runShellOnHosts v
    }

    /**
     * Runs kubectl on the cluster hosts.
     */
    def runShellOnHosts(Map vars) {
        Map v = new HashMap(vars)
        cluster.each { ClusterHost it ->
            v.vars.host = it
            v.target = it.target
            shell v call()
        }
    }

    /**
     * Returns base64 encoded certificates data.
     */
    Map certsData(Tls tls) {
        def renderer = new UriBase64Renderer()
        def map = [:]
        if (tls.cert) {
            map << [cert: renderer.toString(tls.cert, "base64", null)]
        }
        if (tls.key) {
            map << [key: renderer.toString(tls.key, "base64", null)]
        }
        if (tls.ca) {
            map << [ca: renderer.toString(tls.ca, "base64", null)]
        }
        return map
    }

    String getClusterCertsDir(String name) {
        String.format("%s/%s", certsDir, name)
    }

    def getClusterServers() {
        def service = service as ClusterService
        assert service.clusters.size() > 0 : "service.clusters.size()>0"
        service.clusters
    }

    def getClusterTls() {
        def service = service as ClusterService
        assert service.clusters.size() > 0 : "service.clusters.size()>0"
        def credentials = service.clusters.find { it.credentials.hasProperty('tls')  }
        def args = [:]
        if (credentials) {
            args.ca = credentials.tls.ca
            args.caName = credentials.tls.caName
            args.cert = credentials.tls.cert
            args.certName = credentials.tls.certName
            args.key = credentials.tls.key
            args.keyName = credentials.tls.keyName
        }
        return tlsFactory.create(args)
    }

    def getClientTls() {
        getClusterTls()
    }

    String getRobobeeLabelNode() {
        getScriptProperty 'robobee_label_node'
    }

    String getKubernetesLabelHostname() {
        getScriptProperty 'kubernetes_label_hostname'
    }

    @Override
    def getLog() {
        log
    }
}
