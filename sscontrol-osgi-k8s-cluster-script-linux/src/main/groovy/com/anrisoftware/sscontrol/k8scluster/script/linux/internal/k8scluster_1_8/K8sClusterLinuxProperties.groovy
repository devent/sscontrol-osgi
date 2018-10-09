package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>K8s-Cluster properties provider from
 * {@code "/k8s_cluster_1_8_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sClusterLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sClusterLinuxProperties.class.getResource("/k8s_cluster_1_8_linux.properties")

    K8sClusterLinuxProperties() {
        super(K8sClusterLinuxProperties.class, RESOURCE)
    }
}
