package com.anrisoftware.sscontrol.utils.centos.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.google.inject.assistedinject.Assisted

/**
 * CentOS 7 utilities.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Centos_7_Utils extends CentosUtils {

    @Inject
    Centos_7_Properties propertiesProvider

    @Inject
    Centos_7_Utils(@Assisted HostServiceScript script) {
        super(script)
    }

    @Override
    public Properties getDefaultProperties() {
        propertiesProvider.get()
    }
}
