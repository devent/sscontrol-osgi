package com.anrisoftware.sscontrol.hostname.service.external;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Hostname service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Hostname extends HostService {

    /**
     * Returns the host name.
     *
     * @return the host name.
     */
    String getHostname();
}
