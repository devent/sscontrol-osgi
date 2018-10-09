package com.anrisoftware.sscontrol.collectd.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Collectd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Collectd extends HostService {

    /**
     * Returns the version of the service.
     */
    String getVersion();

    /**
     * Returns the configurations.
     */
    List<Config> getConfigs();
}
