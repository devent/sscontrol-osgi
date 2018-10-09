package com.anrisoftware.sscontrol.hostname.script.systemd.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.hostname.service.external.Hostname

import groovy.util.logging.Slf4j

/**
 * Configures the <i>hostname</i> service systems that use systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Hostname_Systemd extends ScriptBase {

    def restartService() {
        log.info 'Set hostname to {}.', service.hostname
        shell privileged: true, "hostnamectl set-hostname $service.hostname" call()
    }

    @Override
    Hostname getService() {
        super.getService()
    }

    @Override
    def getLog() {
        log
    }
}
