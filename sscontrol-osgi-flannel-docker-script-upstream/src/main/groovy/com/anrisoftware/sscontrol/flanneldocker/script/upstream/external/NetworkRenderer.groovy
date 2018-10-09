package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external

import com.anrisoftware.resources.st.external.AttributeRenderer
import com.anrisoftware.sscontrol.flanneldocker.service.external.Network

/**
 * Renders Network.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class NetworkRenderer implements AttributeRenderer {

    @Override
    String toString(Object o, String formatString, Locale locale) {
        Network network = o
        network.address
    }

    @Override
    Class<?> getAttributeType() {
        Network
    }
}
