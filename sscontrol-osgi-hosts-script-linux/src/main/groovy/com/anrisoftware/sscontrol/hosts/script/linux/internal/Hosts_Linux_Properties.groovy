package com.anrisoftware.sscontrol.hosts.script.linux.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Hosts GNU/Linux</i> properties provider from
 * {@code "/hosts_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Hosts_Linux_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Hosts_Linux_Properties.class.getResource("/hosts_linux_0.properties");

    Hosts_Linux_Properties() {
        super(Hosts_Linux_Properties.class, RESOURCE);
    }
}
