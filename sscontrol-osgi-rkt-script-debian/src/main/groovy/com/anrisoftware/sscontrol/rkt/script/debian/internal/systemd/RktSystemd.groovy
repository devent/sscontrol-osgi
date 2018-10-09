package com.anrisoftware.sscontrol.rkt.script.debian.internal.systemd

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * rkt using systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class RktSystemd extends ScriptBase {

    SystemdUtils systemd

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def stopServices() {
        systemd.stopServices()
    }

    def startServices() {
        systemd.startServices()
    }

    @Override
    def getLog() {
        log
    }
}
