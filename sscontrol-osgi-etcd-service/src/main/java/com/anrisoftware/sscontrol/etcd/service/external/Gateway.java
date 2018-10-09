package com.anrisoftware.sscontrol.etcd.service.external;

import java.util.List;

/**
 * <i>Etcd</i> gateway.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Gateway {

    List<String> getEndpoints();
}
