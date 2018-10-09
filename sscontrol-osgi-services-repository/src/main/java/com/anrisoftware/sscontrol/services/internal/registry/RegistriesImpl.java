package com.anrisoftware.sscontrol.services.internal.registry;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.services.internal.targets.AbstractTargetsImpl;
import com.anrisoftware.sscontrol.types.registry.external.Registries;
import com.anrisoftware.sscontrol.types.registry.external.RegistriesService;
import com.anrisoftware.sscontrol.types.registry.external.Registry;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;

/**
 * Image registries targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RegistriesImpl extends AbstractTargetsImpl<RegistryHost, Registry>
        implements Registries {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface RegistriesImplFactory extends RegistriesService {

    }

    @Inject
    RegistriesImpl() {
    }
}
