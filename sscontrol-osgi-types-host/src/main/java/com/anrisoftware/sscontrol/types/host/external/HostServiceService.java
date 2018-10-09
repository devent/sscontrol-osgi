package com.anrisoftware.sscontrol.types.host.external;

import java.util.Map;

/**
 * Creates the host service.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface HostServiceService {

    String getName();

    HostService create(Map<String, Object> args);
}
