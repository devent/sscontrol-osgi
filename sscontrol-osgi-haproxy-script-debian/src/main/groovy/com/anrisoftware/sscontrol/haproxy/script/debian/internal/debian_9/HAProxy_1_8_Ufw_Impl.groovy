package com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9;

import static com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.HAProxy_1_8_Debian_9_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external.HAProxy_1_8_Ufw

import groovy.util.logging.Slf4j

/**
 * Nfs 1.3 Ufw on Debian 9 systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class HAProxy_1_8_Ufw_Impl extends HAProxy_1_8_Ufw {
    
    @Inject
    HAProxy_1_8_Debian_9_Properties propertiesProvider
    
    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def run() {
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
