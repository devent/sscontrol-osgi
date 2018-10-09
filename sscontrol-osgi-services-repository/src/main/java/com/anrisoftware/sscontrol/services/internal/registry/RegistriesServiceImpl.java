package com.anrisoftware.sscontrol.services.internal.registry;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.services.internal.registry.RegistriesImpl.RegistriesImplFactory;
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule;
import com.anrisoftware.sscontrol.types.registry.external.Registries;
import com.anrisoftware.sscontrol.types.registry.external.RegistriesService;

/**
 * Creates the image registries repositories.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(RegistriesService.class)
public class RegistriesServiceImpl implements RegistriesService {

    @Inject
    private RegistriesImplFactory targetsFactory;

    private Registries targets;

    @Override
    public synchronized Registries create() {
        if (targets == null) {
            this.targets = targetsFactory.create();
        }
        return targets;
    }

    @Activate
    protected void start() {
        createInjector(new TargetsModule()).injectMembers(this);
    }
}
