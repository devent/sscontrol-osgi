package com.anrisoftware.sscontrol.types.registry.external;

import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * Container registry target host.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface RegistryHost extends TargetHost {

    /**
     * The type of the repository.
     */
    String getType();
}
