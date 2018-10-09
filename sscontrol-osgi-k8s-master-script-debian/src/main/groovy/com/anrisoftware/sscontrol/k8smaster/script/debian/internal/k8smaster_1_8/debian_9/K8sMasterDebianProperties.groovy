package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>K8s-Master 1.8 Debian 9</i> properties provider from
 * {@code "/k8s_master_1_8_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sMasterDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sMasterDebianProperties.class.getResource("/k8s_master_1_8_debian_9.properties")

    K8sMasterDebianProperties() {
        super(K8sMasterDebianProperties.class, RESOURCE)
    }
}
