package com.anrisoftware.sscontrol.docker.service.external;

import java.util.List;

/**
 * Docker registry.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Registry {

    /**
     * Mirror hosts.
     */
    List<Mirror> getMirrorHosts();
}
