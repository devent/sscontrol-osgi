package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian.Ufw_Fail2ban_0_9_Debian
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Ufw_Fail2ban_Debian_9 extends Ufw_Fail2ban_0_9_Debian {

    @Inject
    Ufw_Fail2ban_Debian_9_Properties debianPropertiesProvider

    @Inject
    TemplatesFactory templatesFactory

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        installPackages()
        configureService()
        configureActions()
        enableService()
    }

    def configureActions() {
        def t = templatesFactory.create 'Ufw_Fail2ban_Debian_9_Templates'
        template resource: t.getResource('ufw_action'), name: "ufwAction", privileged: true, dest: "$actionsDir/ufw.conf" call()
    }

    File getActionsDir() {
        properties.getFileProperty "actions_dir", defaultProperties
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
