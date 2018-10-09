package com.anrisoftware.sscontrol.utils.centos.external

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * CentOS 7 properties provider from
 * {@code "/centos_7_utils.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Centos_7_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Centos_7_Properties.class.getResource("/centos_7_utils.properties")

    Centos_7_Properties() {
        super(Centos_7_Properties.class, RESOURCE)
    }
}
