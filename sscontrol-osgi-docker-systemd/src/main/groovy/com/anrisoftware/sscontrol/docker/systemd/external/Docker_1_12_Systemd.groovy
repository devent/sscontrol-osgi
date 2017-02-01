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

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the Docker 1.12 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Docker_1_12_Systemd extends ScriptBase {

    def stopServices() {
        log.info 'Stopping Docker services.'
        [
            'docker',
        ].each {
            shell privileged: true, "if systemctl list-unit-files --type=service|grep $it; then systemctl stop $it; fi" call()
        }
    }

    def startServices() {
        log.info 'Starting Docker services.'
        [
            'docker',
        ].each {
            shell privileged: true, "systemctl start $it && systemctl status $it && systemctl enable $it" call()
        }
    }

    @Override
    def getLog() {
        log
    }
}
