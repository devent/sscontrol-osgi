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
package com.anrisoftware.sscontrol.sshd.sshd_6.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.sshd.external.Sshd

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6.0 service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Sshd_6 extends ScriptBase {

    def restartService() {
        log.info 'Restarting sshd service.'
        shell privileged: true, "service sshd restart" call()
        shell privileged: true, "service sshd status" call()
    }

    def configureService() {
        log.info 'Configuring sshd service.'
        Sshd service = service
        int debug = service.debugLogging.modules['debug'].level
        replace "s/(?m)^#?\\s*LogLevel.*/LogLevel ${logLevelMap[debug]}/", privileged: true, dest: configFile call()
        replace "s/(?m)^#?\\s*PermitRootLogin.*/PermitRootLogin ${permitRootLogin}/", privileged: true, dest: configFile call()
        replace "s/(?m)^#?\\s*PasswordAuthentication.*/PasswordAuthentication ${passwordAuthentication}/", privileged: true, dest: configFile call()
    }

    Map getLogLevelMap() {
        def p = defaultProperties.getProperty('log_level_map')
        Eval.me p
    }

    String getPermitRootLogin() {
        defaultProperties.getProperty('permit_root_login')
    }

    String getPasswordAuthentication() {
        defaultProperties.getProperty('password_authentication')
    }

    @Override
    Sshd getService() {
        super.getService();
    }

    @Override
    def getLog() {
        log
    }
}
