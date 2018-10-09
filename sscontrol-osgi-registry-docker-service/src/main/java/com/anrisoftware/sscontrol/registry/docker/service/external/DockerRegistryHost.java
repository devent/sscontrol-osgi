package com.anrisoftware.sscontrol.registry.docker.service.external;

import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;

/**
 * <i>Docker</i> registry host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface DockerRegistryHost extends RegistryHost {

    DockerRegistry getRegistry();

}
