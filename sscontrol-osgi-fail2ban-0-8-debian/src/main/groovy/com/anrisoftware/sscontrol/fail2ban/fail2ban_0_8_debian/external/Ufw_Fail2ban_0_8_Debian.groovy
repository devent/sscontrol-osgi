package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.external

import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8.external.Ufw_Fail2ban_0_8

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class Ufw_Fail2ban_0_8_Debian extends Ufw_Fail2ban_0_8 {

    void installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, "apt-get -y install ${packages.join(' ')}" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    @Override
    def getLog() {
        log
    }
}
