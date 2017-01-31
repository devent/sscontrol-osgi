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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Installs Docker 1.12 from the upstream repository for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Docker_1_12_Upstream_Debian_8 extends ScriptBase {

    @Inject
    Docker_1_12_Debian_8_Properties debianPropertiesProvider

    @Override
    Object run() {
        shell """
curl -fsSL $repositoryKey | sudo apt-key add -
sudo add-apt-repository "deb $repository $systemName-$distributionName main"
""" call()
        shell privileged: true, "apt-get update && apt-get -y install docker-engine=$dockerVersion" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    String getRepository() {
        properties.getProperty 'repository', defaultProperties
    }

    String getRepositoryKey() {
        properties.getProperty 'repository_key', defaultProperties
    }

    String getDockerVersion() {
        properties.getProperty 'docker_version', defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
