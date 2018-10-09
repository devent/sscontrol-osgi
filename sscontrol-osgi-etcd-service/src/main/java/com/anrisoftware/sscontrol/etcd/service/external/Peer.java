package com.anrisoftware.sscontrol.etcd.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>Etcd</i> peer.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Peer {

    String getState();

    String getToken();

    Tls getTls();

    List<Binding> getListens();

    List<Binding> getAdvertises();

    List<Cluster> getClusters();

    List<Authentication> getAuthentications();
}
