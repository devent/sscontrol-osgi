package com.anrisoftware.sscontrol.utils.debian.external

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Debian 9 properties provider from
 * {@code "/debian_9_utils.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Debian_9_Properties.class.getResource("/debian_9_utils.properties")

    Debian_9_Properties() {
        super(Debian_9_Properties.class, RESOURCE)
    }
}
