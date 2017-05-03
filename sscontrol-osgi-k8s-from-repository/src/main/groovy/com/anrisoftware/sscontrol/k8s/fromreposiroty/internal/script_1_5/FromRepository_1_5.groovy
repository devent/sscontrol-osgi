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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.FromRepository
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * From repository service for Kubernetes 1.5.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FromRepository_1_5 extends ScriptBase {

    @Inject
    FromRepository_1_5_Properties debianPropertiesProvider

    @Inject
    HostServiceScriptService k8sCluster_1_5_Linux_Service

    Templates templates

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create('MonitoringClusterHeapsterInfluxdbGrafana_1_5_Templates')
    }

    @Override
    def run() {
        FromRepository service = this.service
        def cluster = k8sCluster_1_5_Linux_Service.create(scriptsRepository, service, target, threads, scriptEnv)
        cluster.uploadCertificates credentials: service.cluster.cluster.credentials, clusterName: service.cluster.cluster.cluster.name
        File dir = getState "${service.repo.type}-${service.repo.repo.group}-dir"
        try {
            kubeFiles(dir, cluster)
        } finally {
            shell "rm -rf $dir" call() out
        }
    }

    /**
     * Apply kubectl files.
     */
    def kubeFiles(File dir, HostServiceScript cluster) {
        FromRepository service = this.service
        def files = shell outString: true, chdir: dir,
        vars: [patterns: kubectlFilesPatterns],
        st: "find . <vars.patterns:{p|-name <\\u005C>*.<p>};separator=\" ! \">" call() out
        files.split(/\n/).each {
            cluster.runKubectl chdir: dir, service: service, cluster: service.cluster, args: "apply -f $it"
        }
    }

    /**
     * Parse template files.
     */
    def kubeTemplateFiles(File dir, HostServiceScript cluster) {
        FromRepository service = this.service
        def files = shell outString: true, chdir: dir, "find . -name \\*.stg ! -name \\*.yml ! -name \\*.json" call() out
        files.split(/\n/).each {
            cluster.runKubectl chdir: dir, service: service, cluster: service.cluster, args: "apply -f $it"
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    List getKubectlFilesPatterns() {
        properties.getListProperty 'kubectl_files_patterns', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
