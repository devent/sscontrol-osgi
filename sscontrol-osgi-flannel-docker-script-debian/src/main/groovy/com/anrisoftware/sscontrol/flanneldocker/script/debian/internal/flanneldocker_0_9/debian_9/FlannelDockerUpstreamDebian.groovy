package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.script.upstream.external.FlannelDocker_0_8_Upstream

import groovy.util.logging.Slf4j

/**
 * Configures the Flannel-Docker 0.8 service from the upstream sources
 * for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerUpstreamDebian extends FlannelDocker_0_8_Upstream {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        setupDefaults()
        installFlannel()
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
