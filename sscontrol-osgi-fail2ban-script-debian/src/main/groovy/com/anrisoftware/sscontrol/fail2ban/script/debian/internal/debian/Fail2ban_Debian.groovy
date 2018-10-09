package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian

import static org.joda.time.Duration.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_9.external.Fail2ban_0_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Fail2ban</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Fail2ban_Debian extends Fail2ban_0_9 {

    abstract DebianUtils getDebian()

    SystemdUtils systemd

    void installPackages() {
        debian.installPackages()
    }

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def stopService() {
        systemd.stopServices()
    }

    def startService() {
        systemd.startServices()
    }

    def restartService() {
        systemd.restartServices()
    }

    def enableService() {
        systemd.enableServices()
    }

    @Override
    def getLog() {
        log
    }
}
