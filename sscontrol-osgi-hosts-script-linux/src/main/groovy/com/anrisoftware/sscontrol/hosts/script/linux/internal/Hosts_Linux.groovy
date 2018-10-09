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
