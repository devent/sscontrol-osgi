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
package com.anrisoftware.sscontrol.ssh.linux.internal

import static com.anrisoftware.sscontrol.ssh.linux.internal.Ssh_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.external.Ssh

import groovy.util.logging.Slf4j

/**
 * Configures the <i>ssh</i> on GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Ssh_Linux extends ScriptBase {

    @Inject
    Ssh_Linux_Properties sshProperties

    @Override
    ContextProperties getDefaultProperties() {
        sshProperties.get()
    }

    @Override
    def run() {
        Ssh ssh = service
        def facts = facts()()
        target.system.name = facts.system.name
        target.system.version = facts.system.version
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
