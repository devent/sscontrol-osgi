package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian.Fail2ban_Debian
import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_9.external.Ufw_Fail2ban_0_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Fail2ban</i> service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Fail2ban_Debian_9 extends Fail2ban_Debian {

    @Inject
    Fail2ban_Debian_9_Properties debianPropertiesProvider

    @Inject
    Ufw_Fail2ban_Debian_9_Factory ufwDebian

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        setupDefaults()
        stopService()
        installPackages()
        configureService()
        firewallScript.run()
        enableService()
        startService()
    }

    @Override
    Ufw_Fail2ban_0_9 getUfwScript() {
        ufwDebian.create scriptsRepository, service, target, threads, scriptEnv
    }

    @Override
    Properties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
