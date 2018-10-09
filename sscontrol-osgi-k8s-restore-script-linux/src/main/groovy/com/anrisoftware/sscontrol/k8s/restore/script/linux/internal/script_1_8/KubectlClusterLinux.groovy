package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux

import groovy.util.logging.Slf4j

/**
 * Kubectl service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubectlClusterLinux extends AbstractKubectlLinux {

    @Inject
    RestoreLinuxProperties propertiesProvider

    @Override
    Object run() {
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
