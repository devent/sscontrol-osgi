package com.anrisoftware.sscontrol.hostname.script.debian.internal.debian_9;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Hostname Debian 9 properties provider from
 * {@code "/hostname_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Hostname_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Hostname_Debian_9_Properties.class.getResource("/hostname_debian_9.properties");

    Hostname_Debian_9_Properties() {
        super(Hostname_Debian_9_Properties.class, RESOURCE);
    }
}
