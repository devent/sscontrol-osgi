package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.script.upstream.external.Etcd_3_x_Defaults

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.2 service from the upstream sources
 * for Systemd and Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdDefaults extends Etcd_3_x_Defaults {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        setupDefaults()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
