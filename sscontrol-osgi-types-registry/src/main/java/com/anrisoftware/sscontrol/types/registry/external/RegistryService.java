package com.anrisoftware.sscontrol.types.registry.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Container registry service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface RegistryService extends HostService {

    RegistryHost getRepo();

    List<RegistryHost> getRepos();

}
