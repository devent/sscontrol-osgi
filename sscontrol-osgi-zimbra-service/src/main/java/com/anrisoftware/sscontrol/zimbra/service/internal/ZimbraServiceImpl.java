package com.anrisoftware.sscontrol.zimbra.service.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.anrisoftware.sscontrol.zimbra.service.external.Zimbra;
import com.anrisoftware.sscontrol.zimbra.service.external.ZimbraService;
import com.anrisoftware.sscontrol.zimbra.service.internal.ZimbraImpl.ZimbraImplFactory;
import com.google.inject.AbstractModule;
import com.google.j2objc.annotations.Property;

/**
 * Zimbra service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class ZimbraServiceImpl implements ZimbraService {

    static final String SERVICE_NAME = "zimbra";

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
    private ZimbraImplFactory serviceFactory;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Zimbra create(Map<String, Object> args) {
        return (Zimbra) serviceFactory.create(args);
    }

    @Activate
    public void start() {
        createInjector(new ZimbraModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(HostServicesService.class).toProvider(of(hostServicesService));
                bind(TargetsService.class).toProvider(of(targetsService));
                bind(HostServicePropertiesService.class).toProvider(of(hostPropertiesService));
            }
        }).injectMembers(this);
    }
}
