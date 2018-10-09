package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Fail2ban Debian 9 properties provider from
 * {@code "/fail2ban_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fail2ban_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Fail2ban_Debian_9_Properties.class.getResource("/fail2ban_debian_9.properties");

    Fail2ban_Debian_9_Properties() {
        super(Fail2ban_Debian_9_Properties.class, RESOURCE);
    }
}
