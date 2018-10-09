package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.script.upstream.external.FlannelDocker_0_8_Upstream_Systemd
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the Flannel-Docker 0.8 service from the upstream sources
 * for Systemd and Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerUpstreamSystemdDebian extends FlannelDocker_0_8_Upstream_Systemd {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    SystemdUtils systemd

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Override
    Object run() {
        createDirectories()
        uploadEtcdCerts()
        createServices()
        createConfig()
        setupFlannel()
        systemd.reloadDaemon()
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
