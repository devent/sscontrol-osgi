package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Collectd CentOS 7 properties provider from
 * {@code "/collectd_centos_7.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Collectd_Centos_7_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Collectd_Centos_7_Properties.class.getResource("/collectd_centos_7.properties")

    Collectd_Centos_7_Properties() {
        super(Collectd_Centos_7_Properties.class, RESOURCE)
    }
}
