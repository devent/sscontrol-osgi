package com.anrisoftware.sscontrol.utils.debian.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.google.inject.assistedinject.Assisted

/**
 * Debian 8 utilities.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Debian_8_Utils extends DebianUtils {

    @Inject
    Debian_8_Properties propertiesProvider

    @Inject
    Debian_8_Utils(@Assisted HostServiceScript script) {
        super(script)
    }

    @Override
    public Properties getDefaultProperties() {
        propertiesProvider.get()
    }
}
