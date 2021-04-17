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
package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistry
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryHost
import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.ssh.external.SshHost

import groovy.util.logging.Slf4j

/**
 * <i>Docker</i> registry service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class DockerRegistrySudo extends ScriptBase {

    @Inject
    TemplatesProvider templatesProvider

    @Override
    def run() {
    }

    /**
     * Setups the docker defaults.
     */
    Map setupDockerDefaults(Map vars) {
        DockerRegistry service = this.service
        Map v = new HashMap(vars)
        log.info 'Setups service defaults for {}', service
        if (service.registry && !service.registry.port) {
            service.registry.port = defaultRegistryPort
        }
        if (service.host && service.host.client.ca) {
            service.host.client.caName = defaultClientCaName
        }
        if (service.host && service.host.client.cert) {
            service.host.client.certName = defaultClientCertName
        }
        if (service.host && service.host.client.key) {
            service.host.client.keyName = defaultClientKeyName
        }
        return v
    }

    /**
     * Deploys the docker configuration and certificates to the server.
     */
    Map deployDockerConfig(Map vars) {
        DockerRegistry service = this.service
        Map v = new HashMap(vars)
        log.info 'Deploy docker configuration for {}', service
        vars.dockerDir = getDockerDir(service)
        shell privileged: true, """
mkdir -p "${vars.dockerDir}"
chown \${SSH_USER}.\${SSH_USER} "${vars.dockerDir}"
chmod o-rwx "${vars.dockerDir}"
""" call()
        return v
    }

    def deployClientCerts(Map vars) {
        DockerRegistry service = this.service
        if (!service.host) {
            return
        }
        log.info 'Deploy docker host certificates for {}', service
        def a = [:]
        a.dest = vars.dockerDir
        a.tls = service.host.client
        a.name = 'docker-tls'
        uploadTlsCerts a
    }

    /**
     * Builds the docker image.
     */
    Map dockerBuild(Map vars) {
        DockerRegistry service = this.service
        Map v = new HashMap(vars)
        log.info 'Build docker image for {}', service
        v.service = service
        v.name = vars.repo.group
        v.version = getVersion(vars.repo)
        v.user = getDockerBuildUser(service)
        v.registryName = getRegistryName(service)
        shell timeout: timeoutVeryLong, vars: v,
        resource: templatesProvider.get().getResource("docker_build_cmd"),
        name: "dockerBuild" call()
        return v
    }

    /**
     * Pushs the docker image.
     */
    Map dockerPush(Map vars) {
        DockerRegistry service = this.service
        Map v = new HashMap(vars)
        log.info 'Push docker image for {}', service
        v.service = service
        v.name = vars.repo.group
        v.version = getVersion(vars.repo)
        v.user = getDockerBuildUser(service)
        v.registryName = getRegistryName(service)
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
        getScriptNumberProperty "default_registry_port"
    }

    def getDefaultClientCaName() {
        getScriptProperty 'default_client_ca_name'
    }

    def getDefaultClientCertName() {
        getScriptProperty 'default_client_cert_name'
    }

    def getDefaultClientKeyName() {
        getScriptProperty 'default_client_key_name'
    }

    def getDefaultDockerBuildUser() {
        getScriptProperty 'default_docker_build_user'
    }

    File getDockerCommand() {
        getFileProperty 'docker_command'
    }

    @Override
    def getLog() {
        log
    }
}
