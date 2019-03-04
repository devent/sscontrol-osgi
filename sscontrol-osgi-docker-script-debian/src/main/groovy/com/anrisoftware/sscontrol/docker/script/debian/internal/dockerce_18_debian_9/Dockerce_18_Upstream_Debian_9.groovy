package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.docker.script.debian.internal.debian_9.Dockerce_Upstream_Debian_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Installs Docker CE 18 from the upstream repository for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Dockerce_18_Upstream_Debian_9 extends Dockerce_Upstream_Debian_9 {

    @Inject
    Dockerce_18_Debian_9_Properties debianPropertiesProvider

    DebianUtils debian

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create(this)
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
