package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.script.upstream.external.Etcd_3_x_Upstream

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.2 service from the upstream sources
 * for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdUpstreamDebian extends Etcd_3_x_Upstream {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        installEtcd()
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
