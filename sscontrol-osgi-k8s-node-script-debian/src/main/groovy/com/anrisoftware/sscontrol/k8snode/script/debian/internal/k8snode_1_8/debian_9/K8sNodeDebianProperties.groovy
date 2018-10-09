package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>K8s-Node 1.8 Debian 9</i> properties provider from
 * {@code "/k8s_node_1_8_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sNodeDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sNodeDebianProperties.class.getResource("/k8s_node_1_8_debian_9.properties")

    K8sNodeDebianProperties() {
        super(K8sNodeDebianProperties.class, RESOURCE)
    }
}
