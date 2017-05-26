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
package com.anrisoftware.sscontrol.registry.docker.service.internal.linux

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistry
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryHost

import groovy.util.logging.Slf4j

/**
 * <i>Docker</i> registry service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class DockerRegistry_Linux extends ScriptBase {

    @Override
    def run() {
        DockerRegistry service = this.service
        getState "git-${service.group}-dir", dir
    }

    /**
     * Builds the docker image.
     */
    File dockerBuild(Map vars) {
        DockerRegistry service = vars.service
        DockerRegistryHost repo = vars.repo
        log.info 'Build docker image for {}', service
    }

    def setupCredentials(Map vars) {
        DockerRegistryHost repo = vars.repo
        Credentials credentials = repo.repo.credentials
        Map ret = [:]
        if (credentials) {
            ret = "credentials${credentials.type.capitalize()}"(vars)
        }
        return ret
    }

    Map credentialsSsh(Map vars) {
        DockerRegistryHost repo = vars.repo
        Credentials credentials = repo.repo.credentials
        log.info 'Setup credentials {}', credentials
    }

    @Override
    def getLog() {
        log
    }

    File getCheckoutDirectory() {
        properties.getFileProperty "checkout_directory", base, defaultProperties
    }
}
