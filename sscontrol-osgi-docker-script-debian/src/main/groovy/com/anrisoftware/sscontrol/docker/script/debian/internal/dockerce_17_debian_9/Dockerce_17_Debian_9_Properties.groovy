package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker CE 17 Debian 9</i> properties provider from
 * {@code "/dockerce_17_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Dockerce_17_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Dockerce_17_Debian_9_Properties.class.getResource("/dockerce_17_debian_9.properties")

    Dockerce_17_Debian_9_Properties() {
        super(Dockerce_17_Debian_9_Properties.class, RESOURCE)
    }
}
