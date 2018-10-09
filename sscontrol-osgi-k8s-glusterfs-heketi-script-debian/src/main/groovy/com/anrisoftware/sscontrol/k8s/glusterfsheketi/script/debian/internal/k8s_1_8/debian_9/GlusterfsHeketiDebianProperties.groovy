package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.k8s_1_8.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Heketi properties provider from
 * {@code "/glusterfs_heketi_1_8_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GlusterfsHeketiDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = GlusterfsHeketiDebianProperties.class.getResource("/glusterfs_heketi_1_8_debian_9.properties")

    GlusterfsHeketiDebianProperties() {
        super(GlusterfsHeketiDebianProperties.class, RESOURCE)
    }
}
