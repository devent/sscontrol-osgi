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
package com.anrisoftware.sscontrol.etcd.systemd.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.1 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_1_Systemd extends ScriptBase {

    def stopServices() {
        stopSystemdService([
            'etcd',
        ])
    }

    def startServices() {
        def services = ['etcd']
        log.info 'Starting and enabling {}.', services
        services.each {
            shell timeout: timeoutMiddle, privileged: true, """
systemctl start $it
systemctl status $it
if systemctl status $it | grep 'Active: failed'; then
exit 1
else
systemctl enable $it
fi
""" call()
        }
    }

    @Override
    def getLog() {
        log
    }
}
