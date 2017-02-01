package com.anrisoftware.sscontrol.flanneldocker.upstream.external;

import com.anrisoftware.resources.templates.external.AttributeRenderer;
import com.anrisoftware.sscontrol.flanneldocker.external.Network;

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
