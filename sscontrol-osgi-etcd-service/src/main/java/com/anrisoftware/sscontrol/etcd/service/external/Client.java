package com.anrisoftware.sscontrol.etcd.service.external;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>Etcd</i> client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Client {

    Tls getTls();
}
