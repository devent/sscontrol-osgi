package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker CE 18 Debian 9</i> properties provider from
 * {@code "/dockerce_18_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Dockerce_18_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Dockerce_18_Debian_9_Properties.class.getResource("/dockerce_18_debian_9.properties")

    Dockerce_18_Debian_9_Properties() {
        super(Dockerce_18_Debian_9_Properties.class, RESOURCE)
    }
}
