package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Sshd Debian 9</i> properties provider from
 * {@code "/sshd_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SshdDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = SshdDebianProperties.class.getResource("/sshd_debian_9.properties")

    SshdDebianProperties() {
        super(SshdDebianProperties.class, RESOURCE)
    }
}
