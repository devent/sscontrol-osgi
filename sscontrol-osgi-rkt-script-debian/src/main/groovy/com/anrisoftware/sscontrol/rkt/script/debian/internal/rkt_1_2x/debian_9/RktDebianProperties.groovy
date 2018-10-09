package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * rkt 1.28 for debian 9 properties provider from
 * {@code "/rkt_1_2x_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RktDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = RktDebianProperties.class.getResource("/rkt_1_2x_debian_9.properties")

    RktDebianProperties() {
        super(RktDebianProperties.class, RESOURCE)
    }
}
