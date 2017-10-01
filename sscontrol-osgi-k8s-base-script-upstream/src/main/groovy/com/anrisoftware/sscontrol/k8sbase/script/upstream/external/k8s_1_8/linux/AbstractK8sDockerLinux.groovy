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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures Docker.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sDockerLinux extends ScriptBase {

    TemplateResource dockerOptionsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8s_1_8_DockerOptionsTemplates')
        this.dockerOptionsTemplate = templates.getResource('docker_options')
    }

    @Override
    Object run() {
        template privileged: true, resource: dockerOptionsTemplate, name: "dockerOptions", vars: [:], dest: dockerOptionsFile call()
    }

    File getDockerOptionsFile() {
        getFileProperty 'docker_options_file'
    }

    boolean getIptablesDisableDocker() {
        getScriptBooleanProperty 'iptables_disable_docker'
    }

    @Override
    def getLog() {
        log
    }
}
