package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Ufw properties provider from
 * {@code "/ufw_fail2ban_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ufw_Fail2ban_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Ufw_Fail2ban_Debian_9_Properties.class.getResource("/ufw_fail2ban_debian_9.properties");

    Ufw_Fail2ban_Debian_9_Properties() {
        super(Ufw_Fail2ban_Debian_9_Properties.class, RESOURCE);
    }
}
