package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal.Fail2ban_0_8_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.external.Ufw_Fail2ban_0_8_Debian

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Ufw_Fail2ban_0_8_Debian_8 extends Ufw_Fail2ban_0_8_Debian {

    @Inject
    Ufw_Fail2ban_0_8_Debian_8_Properties debianPropertiesProvider

    @Override
    def run() {
        installPackages()
        configureService()
        enableService()
    }

    @Override
    Properties getDefaultProperties() {
        debianPropertiesProvider.get()
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
