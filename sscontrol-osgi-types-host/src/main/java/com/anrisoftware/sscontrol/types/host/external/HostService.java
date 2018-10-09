package com.anrisoftware.sscontrol.types.host.external;

import java.util.List;

/**
 * Host service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostService {

    String getName();

    TargetHost getTarget();

    List<TargetHost> getTargets();

    HostServiceProperties getServiceProperties();
}
