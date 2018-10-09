package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.sshd.service.external.Sshd
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Configures the Ufw firewall.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class SshdDebianUfw extends ScriptBase {

    @Inject
    SshdDebianProperties debianPropertiesProvider

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        Sshd service = service
        if (!ufw.ufwActive) {
            log.debug 'No Ufw available.'
            return
        }
        updateFirewall()
    }

    def updateFirewall() {
        Sshd service = service
        if (service.binding.port) {
            ufw.ufwAllowPortsToAny([service.binding.port], this)
        }
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
