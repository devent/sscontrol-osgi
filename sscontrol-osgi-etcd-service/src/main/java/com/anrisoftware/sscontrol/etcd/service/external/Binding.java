package com.anrisoftware.sscontrol.etcd.service.external;

import java.net.URI;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Binding {

    String getNetwork();

    URI getAddress();
}
