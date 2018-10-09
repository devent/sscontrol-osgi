package com.anrisoftware.sscontrol.hosts.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * <i>Hosts</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Hosts extends HostService {

    /**
     * Returns the hosts.
     */
    List<Host> getHosts();
}
