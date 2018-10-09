package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Zimbra 8.7 for CentOS 7 Upstream properties provider from
 * {@code "/zimbra_8_7_centos_7_upstream.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Zimbra_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Zimbra_Properties.class.getResource("/zimbra_8_7_centos_7_upstream.properties")

    Zimbra_Properties() {
        super(Zimbra_Properties.class, RESOURCE)
    }
}
