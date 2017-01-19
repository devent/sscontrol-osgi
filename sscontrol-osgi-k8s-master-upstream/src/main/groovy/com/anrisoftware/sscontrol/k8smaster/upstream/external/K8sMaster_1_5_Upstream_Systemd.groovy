/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master 1.5 service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMaster_1_5_Upstream_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8sMaster_1_5_Upstream_Systemd_Templates')
        this.servicesTemplate = templates.getResource('k8s_master_services')
        this.configsTemplate = templates.getResource('k8s_master_configs')
    }

    def createServices() {
        log.info 'Create k8s-master services.'
        def dir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        shell privileged: true, """
mkdir -p '$dir'
mkdir -p '$tmpdir'
mkdir -p '$runDir'
chown $user '$runDir'
useradd -r $user
""" call()
        [
            [
                resource: servicesTemplate,
                name: 'kubeApiserverService',
                privileged: true,
                override: false,
                dest: "$dir/kube-apiserver.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeControllerManagerService',
                privileged: true,
                override: false,
                dest: "$dir/kube-controller-manager.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeSchedulerService',
                privileged: true,
                override: false,
                dest: "$dir/kube-scheduler.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'servicesKubernetesConf',
                privileged: true,
                override: false,
                dest: "$tmpdir/kubernetes.conf",
                vars: [:],
            ],
        ].each { template it call() }
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def createConfig() {
        log.info 'Create k8s-master configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: configsTemplate,
                name: 'kubeConfig',
                privileged: true,
                override: false,
                dest: "$configDir/config",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeApiserverConfig',
                privileged: true,
                override: false,
                dest: "$configDir/apiserver",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeControllerManagerConfig',
                privileged: true,
                override: false,
                dest: "$configDir/controller-manager",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeSchedulerConfig',
                privileged: true,
                override: false,
                dest: "$configDir/scheduler",
                vars: [:],
            ],
        ].each { template it call() }
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
