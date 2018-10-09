package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * rkt 1.28 for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RktDebian extends ScriptBase {

    @Inject
    RktDebianProperties debianPropertiesProvider

    RktSystemdDebian systemd

    @Inject
    RktUpstreamDebianFactory upstreamFactory

    DebianUtils debian

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        systemd.stopServices()
        debian.installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemd.startServices()
    }

    @Inject
    def setSystemdFactory(RktSystemdDebianFactory systemdFactory) {
        this.systemd = systemdFactory.create(scriptsRepository, service, target, threads, scriptEnv)
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
