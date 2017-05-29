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

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistry
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryHost
import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost
import com.anrisoftware.sscontrol.types.ssh.external.SshHost

import groovy.util.logging.Slf4j

/**
 * <i>Docker</i> registry service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class DockerRegistry_Linux extends ScriptBase {

    @Inject
    TemplatesProvider templatesProvider

    @Override
    def run() {
    }

    /**
     * Setups the docker defaults.
     */
    Map setupDockerDefaults(Map vars) {
        Map v = new HashMap(vars)
        RegistryHost registry = service.registry
        DockerRegistry docker = registry.registry
        log.info 'Setups docker defaults for service {}, docker {}', service, docker
        if (docker.registry && !docker.registry.port) {
            docker.registry.port = defaultRegistryPort
        }
        if (docker.host && docker.host.client.ca) {
            docker.host.client.caName = defaultClientCaName
        }
        if (docker.host && docker.host.client.cert) {
            docker.host.client.certName = defaultClientCertName
        }
        if (docker.host && docker.host.client.key) {
            docker.host.client.keyName = defaultClientKeyName
        }
        return v
    }

    /**
     * Deploys the docker configuration and certificates to the server.
     */
    Map deployDockerConfig(Map vars) {
        Map v = new HashMap(vars)
        RegistryHost registry = service.registry
        DockerRegistry docker = registry.registry
        log.info 'Deploy docker configuration for service {}, docker {}', service, docker
        vars.dockerDir = getDockerDir(docker)
        shell privileged: true, """
mkdir -p "${vars.dockerDir}"
chown \${SSH_USER}.\${SSH_USER} "${vars.dockerDir}"
chmod o-rwx "${vars.dockerDir}"
""" call()
        return v
    }

    def deployClientCerts(Map vars) {
        RegistryHost registry = service.registry
        DockerRegistry docker = registry.registry
        if (!docker.host) {
            return
        }
        log.info 'Deploy docker host certificates for service {}, docker {}', service, docker
        def a = [:]
        a.dest = vars.dockerDir
        a.tls = docker.host.client
        a.name = 'docker-tls'
        uploadTlsCerts a
    }

    /**
     * Builds the docker image.
     */
    Map dockerBuild(Map vars) {
        Map v = new HashMap(vars)
        RegistryHost registry = service.registry
        DockerRegistry docker = registry.registry
        log.info 'Build docker image for service {}, docker {}', service, docker
        v.service = docker
        v.name = vars.repo.group
        v.version = getVersion(vars.repo)
        v.user = getDockerBuildUser(docker)
        v.registryName = getRegistryName(docker)
        shell timeout: timeoutVeryLong, vars: v,
        resource: templatesProvider.get().getResource("docker_build_cmd"),
        name: "dockerBuild" call()
        return v
    }

    /**
     * Pushs the docker image.
     */
    Map dockerPush(Map vars) {
        Map v = new HashMap(vars)
        RegistryHost registry = service.registry
        DockerRegistry docker = registry.registry
        log.info 'Push docker image for service {}, docker {}', service, docker
        v.service = docker
        v.name = vars.repo.group
        v.version = getVersion(vars.repo)
        v.user = getDockerBuildUser(docker)
        v.registryName = getRegistryName(docker)
        shell timeout: timeoutVeryLong, vars: v,
        resource: templatesProvider.get().getResource("docker_push_cmd"),
        name: "dockerPush" call()
        return v
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

    /**
     * Returns the docker configuration directory for the docker service.
     * Defaults to @{code /var/lib/robobee/docker/HOST}
     */
    File getDockerDir(DockerRegistry docker) {
        def dir = getFileProperty "docker_config_dir"
        def host = docker.host ? docker.host.address.toString() : 'localhost'
        new File(dir, host)
    }

    String getDockerBuildUser(DockerRegistry docker) {
        if (!docker.host) {
            return defaultDockerBuildUser
        }
        switch (docker.host) {
            case 'unix:///var/run/docker.sock':
                return defaultDockerBuildUser
            default:
                if (docker.target instanceof SshHost) {
                    return docker.target.user
                }
        }
    }

    String getRegistryName(DockerRegistry docker) {
        if (docker.group != 'default') {
            return "${docker.group}/"
        } else {
            return ''
        }
    }

    String getVersion(HostService repo) {
        if (repo.checkout.branch) {
            return repo.checkout.branch
        }
        if (repo.checkout.tag) {
            return repo.checkout.tag
        }
        if (repo.checkout.commit) {
            return repo.checkout.commit
        }
        return 'master'
    }

    int getDefaultRegistryPort() {
        properties.getNumberProperty "default_registry_port", defaultProperties
    }

    def getDefaultClientCaName() {
        properties.getProperty 'default_client_ca_name', defaultProperties
    }

    def getDefaultClientCertName() {
        properties.getProperty 'default_client_cert_name', defaultProperties
    }

    def getDefaultClientKeyName() {
        properties.getProperty 'default_client_key_name', defaultProperties
    }

    def getDefaultDockerBuildUser() {
        properties.getProperty 'default_docker_build_user', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
