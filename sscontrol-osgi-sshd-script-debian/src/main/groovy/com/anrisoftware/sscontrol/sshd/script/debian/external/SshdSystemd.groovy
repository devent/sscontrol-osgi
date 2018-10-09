package com.anrisoftware.sscontrol.sshd.script.debian.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.sshd.script.openssh.external.OpensshSystemd
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class SshdSystemd extends OpensshSystemd {

    SystemdUtils systemd

    abstract DebianUtils getDebian()

    void installPackages() {
        debian.installPackages()
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def startService() {
        systemd.startServices()
    }

    def restartService() {
        systemd.restartServices()
    }

    def stopService() {
        systemd.stopServices()
    }

    def enableService() {
        systemd.enableServices()
    }

    @Override
    def getLog() {
        log
    }
}
