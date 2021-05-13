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
package com.anrisoftware.sscontrol.crio.script.crio_1_20

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.crio.service.external.Crio
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * CRI-O 1.20.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Crio_1_20 extends ScriptBase {

    TemplateResource crioConfTemplate

    TemplateResource kubernetesCriTemplate

    TemplateResource cgroupManagerTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create("Crio_1_20_K8s_Templates")
        this.crioConfTemplate = templates.getResource("crio_conf")
        this.kubernetesCriTemplate = templates.getResource("kubernetes_cri_conf")
        this.cgroupManagerTemplate = templates.getResource("cgroup_manager_conf")
    }

    /**
     * Configures CRI-O for Kubernetes.
     */
    def configureK8s() {
        Crio service = this.service
        template resource: crioConfTemplate, name: "crioConf", privileged: true, dest: k8sModulesFile, vars: [:] call()
        template resource: kubernetesCriTemplate, name: "kubernetesCriConf", privileged: true, dest: k8sSysctlFile, vars: [:] call()
        k8sModules.each { shell privileged: true, "modprobe $it" call() }
        shell privileged: true, "sysctl --system" call()
    }

    /**
     * Configures the cgroup driver.
     */
    def configureCgroupDriver() {
        Crio service = this.service
        template resource: cgroupManagerTemplate, name: "cgroupManagerConf", privileged: true, dest: cgroupManagerFile, vars: [:] call()
    }

    /**
     * Returns the Linux modules file for Kubernetes configuration.
     * For example <code>/etc/modules-load.d/crio.conf</code>
     * <ul>
     * <li><code>k8s_modules_file</code>
     * </ul>
     */
    File getK8sModulesFile() {
        getScriptFileProperty 'k8s_modules_file'
    }

    /**
     * Returns the Linux file to modify kernel parameters for Kubernetes configuration.
     * For example <code>/etc/sysctl.d/99-kubernetes-cri.conf</code>
     * <ul>
     * <li><code>k8s_sysctl_file</code>
     * </ul>
     */
    File getK8sSysctlFile() {
        getScriptFileProperty 'k8s_sysctl_file'
    }

    /**
     * Returns the Linux modules to enable for Kubernetes configuration.
     * For example <code>overlay, br_netfilter</code>
     * <ul>
     * <li><code>k8s_modules</code>
     * </ul>
     */
    List getK8sModules() {
        getScriptListProperty 'k8s_modules'
    }

    /**
     * Returns the kernel parameters to enable for Kubernetes configuration.
     * For example <code>net.bridge.bridge-nf-call-iptables=1,net.ipv4.ip_forward=1,net.bridge.bridge-nf-call-ip6tables=1</code>
     * <ul>
     * <li><code>k8s_sysctl_cri</code>
     * </ul>
     */
    List getK8sSysctlCri() {
        getScriptListProperty 'k8s_sysctl_cri'
    }

    /**
     * Returns the cgroup driver configuration file.
     * <ul>
     * <li><code>cgroup_manager_file</code>
     * </ul>
     */
    File getCgroupManagerFile() {
        getScriptFileProperty 'cgroup_manager_file', configDir
    }

    /**
     * <ul>
     * <li><code>conmon_cgroup</code>
     * </ul>
     */
    String getConmonCgroup() {
        getScriptProperty 'conmon_cgroup'
    }

    /**
     * <ul>
     * <li><code>cgroup_manager</code>
     * </ul>
     */
    String getCgroupManager() {
        getScriptProperty 'cgroup_manager'
    }

    @Override
    def getLog() {
        log
    }
}
