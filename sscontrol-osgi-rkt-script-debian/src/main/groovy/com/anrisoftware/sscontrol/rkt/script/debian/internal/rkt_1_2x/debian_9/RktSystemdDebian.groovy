package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.rkt.script.debian.internal.systemd.RktSystemd

import groovy.util.logging.Slf4j

/**
 * rkt 1.28 using systemd and Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RktSystemdDebian extends RktSystemd {

    @Inject
    RktDebianProperties debianPropertiesProvider

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
