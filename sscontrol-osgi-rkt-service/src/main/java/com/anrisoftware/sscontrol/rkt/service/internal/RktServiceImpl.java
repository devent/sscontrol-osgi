package com.anrisoftware.sscontrol.rkt.service.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.sscontrol.rkt.service.external.Rkt;
import com.anrisoftware.sscontrol.rkt.service.external.RktService;
import com.anrisoftware.sscontrol.rkt.service.internal.RktImpl.RktImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;
import com.google.j2objc.annotations.Property;

/**
 * Creates the rkt service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class RktServiceImpl implements RktService {

    static final String SERVICE_NAME = "rkt";

    @Property(value = SERVICE_NAME)
    static final String NAME_PROPERTY = "service.name";

    private String name;

    @Reference
    private HostServicesService hostServicesService;

    @Reference
    private TargetsService targetsService;

    @Reference
    private HostServicePropertiesService hostPropertiesService;

    @Inject
    private RktImplFactory hostnameFactory;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Rkt create(Map<String, Object> args) {
        return (Rkt) hostnameFactory.create(args);
    }

    @Activate
    public void start() {
        createInjector(new RktModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(HostServicesService.class).toProvider(of(hostServicesService));
                bind(TargetsService.class).toProvider(of(targetsService));
                bind(HostServicePropertiesService.class).toProvider(of(hostPropertiesService));
            }
        }).injectMembers(this);
    }
}
