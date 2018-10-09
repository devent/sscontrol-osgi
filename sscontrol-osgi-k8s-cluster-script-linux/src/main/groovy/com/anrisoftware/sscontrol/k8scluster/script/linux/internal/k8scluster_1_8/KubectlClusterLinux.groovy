package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Cluster service from the upstream sources for GNU/Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubectlClusterLinux extends AbstractKubectlLinux {

    @Inject
    K8sClusterLinuxProperties linuxPropertiesProvider

    @Override
    Object run() {
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
