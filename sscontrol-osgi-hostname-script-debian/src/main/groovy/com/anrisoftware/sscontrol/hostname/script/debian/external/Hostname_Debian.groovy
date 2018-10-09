package com.anrisoftware.sscontrol.hostname.script.debian.external

import com.anrisoftware.sscontrol.hostname.script.systemd.external.Hostname_Systemd
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * Configures the <i>hostname</i> on Debian systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Hostname_Debian extends Hostname_Systemd {

    abstract DebianUtils getDebian()

    @Override
    def run() {
        installPackages()
        restartService()
    }

    void installPackages() {
        log.info "Installing packages {}.", packages
        debian.installPackages()
    }

    @Override
    def getLog() {
        log
    }
}
