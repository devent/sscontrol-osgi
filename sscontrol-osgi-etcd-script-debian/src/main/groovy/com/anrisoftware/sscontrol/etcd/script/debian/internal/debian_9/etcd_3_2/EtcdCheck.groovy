package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.script.upstream.external.Etcd_3_x_Check

import groovy.util.logging.Slf4j

/**
 * Checks the etcd cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdCheck extends Etcd_3_x_Check {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
