package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.8 service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerDebian extends ScriptBase {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    @Inject
    FlannelDockerUpstreamDebianFactory upstreamFactory

    @Inject
    FlannelDockerUpstreamSystemdDebianFactory upstreamSystemdFactory

    @Inject
    FlannelDockerUfwFactory ufwFactory

    @Inject
    IperfConnectionCheckFactory iperfConnectionCheckFactory

    DebianUtils debian

    SystemdUtils systemd

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Override
    def run() {
        systemd.stopServices()
        debian.installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamSystemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemd.startServices()
        iperfConnectionCheckFactory.create(scriptsRepository, service, target, threads, scriptEnv).checkFlannel()
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
