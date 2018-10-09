package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Collectd Debian 9 properties provider from
 * {@code "/collectd_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Collectd_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Collectd_Debian_9_Properties.class.getResource("/collectd_debian_9.properties")

    Collectd_Debian_9_Properties() {
	super(Collectd_Debian_9_Properties.class, RESOURCE)
    }
}
