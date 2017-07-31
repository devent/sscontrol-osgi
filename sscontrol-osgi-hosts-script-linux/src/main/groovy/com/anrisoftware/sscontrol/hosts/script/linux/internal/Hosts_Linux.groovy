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
package com.anrisoftware.sscontrol.hosts.script.linux.internal

import static com.anrisoftware.sscontrol.hosts.script.linux.internal.Hosts_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.hosts.service.external.Host

import groovy.util.logging.Slf4j

/**
 * Configures the <i>hosts</i> on GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Hosts_Linux extends ScriptBase {

    @Inject
    Hosts_Linux_Properties hostsPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        hostsPropertiesProvider.get()
    }

    @Override
    def run() {
        replace privileged: true, dest: configFile with {
            service.hosts.each { Host h ->
                def address = h.address
                def host = h.host
                def aliases = h.aliases.join(' ')
                switch (h.identifier) {
                    case 'host':
                        line "s/.*($host).*/$address $host $aliases/"
                        break
                    case 'address':
                        line "s/.*($address).*/$address $host $aliases/"
                        break
                }
            }
            it
        }()
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
