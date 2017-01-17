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
package com.anrisoftware.sscontrol.k8smaster.systemd.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Master</i> version 1.5 service using systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMaster_1_5_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8sMaster_1_5_Systemd_Templates')
        this.servicesTemplate = templates.getResource('k8s_master_services')
        this.configsTemplate = templates.getResource('k8s_master_configs')
    }

    def restartServices() {
        log.info 'Restarting k8s services.'
        [
            'kube-apiserver',
            'kube-controller-manager',
            'kube-scheduler'
        ].each {
            shell privileged: true, "service $it restart" call()
            shell privileged: true, "service $it status" call()
        }
    }

    def createServices() {
        log.info 'Create k8s-master services.'
        def systemDir = systemdSystemDir
        [
            [
                resource: servicesTemplate,
                name: 'kubeApiserverService',
                privileged: true,
                override: false,
                dest: "$systemDir/kube-apiserver.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeControllerManagerService',
                privileged: true,
                override: false,
                dest: "$systemDir/kube-controller-manager.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeSchedulerService',
                privileged: true,
                override: false,
                dest: "$systemDir/kube-scheduler.service",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createConfig() {
        log.info 'Create k8s-master configuration.'
        def dir = configDir
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

    def configureServices() {
        log.info 'Configure k8s services.'
        K8sMaster service = service
        def dir = configDir
        def dest = "$dir/config"
        replace dest: "$dir/config", privileged: true with {
            [
                [
                    key: 'KUBE_LOG_LEVEL',
                    entry: { "--v=$it" },
                    value: service.debugLogging.modules['debug'].level
                ],
                [
                    key: 'KUBE_ALLOW_PRIV',
                    entry: { "--allow-privileged=$it" },
                    value: service.allowPrivileged
                ],
            ].each {
                log.info 'Replace entry {} in {}', it.entry, dest
                line "s/(?m)^#?${it.key}=\"--v=\\d*\".*/${it.key}=\"${it.entry(it.value)}\"/"
            }
            it
        }.call()
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
