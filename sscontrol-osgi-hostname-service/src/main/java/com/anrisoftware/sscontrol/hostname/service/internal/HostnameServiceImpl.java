package com.anrisoftware.sscontrol.hostname.service.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.sscontrol.hostname.service.external.Hostname;
import com.anrisoftware.sscontrol.hostname.service.external.HostnameService;
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;
import com.google.j2objc.annotations.Property;

/**
 * Creates the hostname service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class HostnameServiceImpl implements HostnameService {

    static final String HOSTNAME_NAME = "hostname";

    @Property(value = HOSTNAME_NAME)
    static final String NAME_PROPERTY = "service.name";

    private String name;

    @Reference
    private HostServicesService hostServicesService;

    @Reference
    private TargetsService targetsService;

    @Reference
    private HostServicePropertiesService hostPropertiesService;

    @Inject
    private HostnameImplFactory hostnameFactory;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Hostname create(Map<String, Object> args) {
        return (Hostname) hostnameFactory.create(args);
    }

    @Activate
    public void start() {
        createInjector(new HostnameModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(HostServicesService.class).toProvider(of(hostServicesService));
                bind(TargetsService.class).toProvider(of(targetsService));
                bind(HostServicePropertiesService.class).toProvider(of(hostPropertiesService));
            }
        }).injectMembers(this);
    }
}
