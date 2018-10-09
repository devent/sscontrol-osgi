package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian

import static org.joda.time.Duration.*

import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_9.external.Ufw_Fail2ban_0_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class Ufw_Fail2ban_0_9_Debian extends Ufw_Fail2ban_0_9 {

    abstract DebianUtils getDebian()

    def installPackages() {
        debian.installPackages()
    }

    @Override
    def getLog() {
        log
    }
}
