package com.anrisoftware.sscontrol.etcd.service.external;

import java.util.List;

/**
 * <i>Etcd</i> grpc-proxy.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Proxy {

    String getNamespace();

    List<String> getEndpoints();
}
