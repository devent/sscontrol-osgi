package com.anrisoftware.sscontrol.services.internal.host;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;

/**
 * Creates the host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(HostServicesService.class)
public class HostServicesServiceImpl implements HostServicesService {

    @Reference
    private TargetsService targetsService;

    @Inject
    private HostServicesImplFactory repositoryFactory;

    private HostServices scriptsRepository;

    @Override
    public synchronized HostServices create() {
        if (scriptsRepository == null) {
            this.scriptsRepository = repositoryFactory.create();
        }
        return scriptsRepository;
    }

    @Activate
    protected void start() {
        createInjector(new HostServicesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TargetsService.class).toProvider(of(targetsService));
            }
        }).injectMembers(this);
    }
}
