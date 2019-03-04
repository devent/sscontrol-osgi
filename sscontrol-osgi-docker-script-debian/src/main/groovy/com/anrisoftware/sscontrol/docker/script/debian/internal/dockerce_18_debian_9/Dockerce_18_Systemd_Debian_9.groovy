package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.docker.script.systemd.external.Dockerce_17_Systemd

import groovy.util.logging.Slf4j

/**
 * Configures the Docker CE 18 service using Systemd and Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Dockerce_18_Systemd_Debian_9 extends Dockerce_17_Systemd {

    @Inject
    Dockerce_18_Debian_9_Properties debianPropertiesProvider

    @Override
    Object run() {
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
