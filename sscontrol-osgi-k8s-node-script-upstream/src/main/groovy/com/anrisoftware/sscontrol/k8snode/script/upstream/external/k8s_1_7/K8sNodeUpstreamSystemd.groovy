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
package com.anrisoftware.sscontrol.k8snode.script.upstream.external.k8s_1_7

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUpstreamLinux
import com.anrisoftware.sscontrol.k8snode.service.external.K8sNode

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Node service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sNodeUpstreamSystemd extends AbstractK8sUpstreamLinux {

    def setupClusterDefaults() {
        K8sNode service = service
        assertThat("cluster api-servers=null", service.cluster.apiServers, not(empty()))
        super.setupClusterDefaults()
    }

    def createKubeletManifests() {
        log.info 'Create kubelet manifests files.'
        K8sNode service = service
        def dir = manifestsDir
        def srv = srvManifestsDir
        shell privileged: true, "mkdir -p $dir; mkdir -p $srv" call()
        def templates = [
            [
                resource: manifestsTemplate,
                name: 'kubeProxyManifest',
                privileged: true,
                dest: "$dir/kube-proxy.yaml",
                vars: [:],
            ],
        ]
        templates.each { template it call() }
    }

    File getKubeconfigFile() {
        properties.getFileProperty "kubeconfig_file", configDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
