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
package com.anrisoftware.sscontrol.docker.systemd.external

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.docker.external.Docker
import com.anrisoftware.sscontrol.docker.external.Mirror
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures the Docker 1.12 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Docker_1_12_Systemd extends ScriptBase {

    TemplateResource mirrorTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Docker_1_12_Systemd_Templates')
        this.mirrorTemplate = templates.getResource('mirror_config')
    }

    def setupDefaults() {
        Docker service = service
        service.registry.mirrorHosts.each {
            def address = it.host
            def host = address.host ? address.host : address.path
            def proto = address.scheme ? address.scheme : getDefaultMirrorProtocol(it.tls)
            def port = address.port != -1 ? address.port : defaultMirrorPort
            it.host = new URI("$proto://$host:$port")
            Tls tls = it.tls
            if (!tls.caName) {
                tls.caName = defaultRegistryMirrorCaName
            }
        }
    }

    def createDirectories() {
        log.info 'Create docker directories.'
        def dropin = systemdDropinDir
        shell privileged: true, """
mkdir -p '$dropin'
""" call()
    }

    def createRegistryMirrorConfig() {
        Docker service = service
        if (service.registry.mirrorHosts.size() == 0) {
            return
        }
        log.info 'Create docker registry mirror configuration.'
        def dropin = systemdDropinDir
        template resource: mirrorTemplate,
        name: 'mirrorService',
        privileged: true,
        dest: "$dropin/10_mirror.conf",
        vars: [:] call()
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def deployMirrorCerts() {
        Docker service = service
        if (service.registry.mirrorHosts.size() == 0) {
            return
        }
        service.registry.mirrorHosts.each {
            def dir = getCertsDir(it)
            Tls tls = it.tls
            if (tls.ca) {
                log.info 'Deploy mirror certificates for {}.', it
                shell privileged: true, "mkdir -p '$dir'" call()
                copyResource privileged: true, src: tls.ca, dest: "$dir/${tls.caName}" call()
            }
        }
    }

    def stopServices() {
        stopSystemdService 'docker'
    }

    def startServices() {
        startEnableSystemdService 'docker'
    }

    File getSystemdDropinDir() {
        properties.getFileProperty 'systemd_dropin_dir', base, defaultProperties
    }

    File getCertsDir(Mirror mirror) {
        def template = properties.getProperty 'certs_dir_template', defaultProperties
        def host = mirror.host.host != null ? mirror.host.host : mirror.host.path
        def port = mirror.host.port
        def path = new ST(template).add('host', "$host:$port").render()
        base != null ? new File(base, path) : new File(path)
    }

    def getDefaultRegistryMirrorCaName() {
        properties.getProperty 'default_registry_mirror_ca_name', defaultProperties
    }

    def getDefaultMirrorProtocol(Tls tls) {
        if (tls.ca) {
            properties.getProperty 'default_mirror_https_protocol', defaultProperties
        } else {
            properties.getProperty 'default_mirror_http_protocol', defaultProperties
        }
    }

    def getDefaultMirrorPort() {
        properties.getProperty 'default_mirror_port', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
