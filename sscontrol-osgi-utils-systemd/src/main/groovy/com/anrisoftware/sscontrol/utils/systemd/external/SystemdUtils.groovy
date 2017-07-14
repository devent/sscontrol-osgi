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
package com.anrisoftware.sscontrol.utils.systemd.external

import static org.apache.commons.io.FilenameUtils.*
import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j

/**
 * Systemd utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class SystemdUtils {

    final HostServiceScript script

    TemplateResource cmdTemplate

    @Inject
    SystemdUtils(@Assisted HostServiceScript script) {
        this.script = script
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('SystemdUtils')
        this.cmdTemplate = templates.getResource('systemd_cmd')
    }

    /**
     * Reloads the systemd daemon.
     */
    def reloadDaemon() {
        script.shell privileged: true,
        vars: [:],
        resource: cmdTemplate, name: "reloadCmd" call()
    }

    /**
     * Stop the specified service.
     */
    def stopService(String service) {
        stopService([service])
    }

    /**
     * Stop the specified services.
     */
    def stopService(List services) {
        log.info 'Stopping {}.', services
        script.shell privileged: true,
        vars: [services: services],
        resource: cmdTemplate, name: "stopCmd" call()
    }

    /**
     * Start the specified service.
     */
    def startService(String service) {
        startService([service])
    }

    /**
     * Start the specified services.
     */
    def startService(List services) {
        log.info 'Starting {}.', services
        script.shell privileged: true,
        vars: [services: services],
        resource: cmdTemplate, name: "startCmd" call()
    }

    /**
     * Enable the specified service.
     */
    def enableService(String service) {
        enableService([service])
    }

    /**
     * Enable the specified services.
     */
    def enableService(List services) {
        log.info 'Enabling {}.', services
        script.shell privileged: true,
        vars: [services: services],
        resource: cmdTemplate, name: "enableCmd" call()
    }

    /**
     * Restart the specified service.
     */
    def restartService(String service) {
        restartService([service])
    }

    /**
     * Restart the specified services.
     */
    def restartService(List services) {
        log.info 'Restarting {}.', services
        script.shell privileged: true,
        vars: [services: services],
        resource: cmdTemplate, name: "restartCmd" call()
    }
}