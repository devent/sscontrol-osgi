package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Flannel-Docker properties provider from
 * {@code "/flanneldocker_0_9_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FlannelDockerDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = FlannelDockerDebianProperties.class.getResource("/flanneldocker_0_9_debian_9.properties")

    FlannelDockerDebianProperties() {
        super(FlannelDockerDebianProperties.class, RESOURCE)
    }
}
