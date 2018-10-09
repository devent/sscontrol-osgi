package com.anrisoftware.sscontrol.properties.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.propertiesutils.TypedAllPropertiesFactory;
import com.anrisoftware.propertiesutils.TypedAllPropertiesService;
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.google.inject.AbstractModule;

/**
 * Properties service.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServicePropertiesService.class)
public class HostServicePropertiesServiceImpl
        implements HostServicePropertiesService {

    @Inject
    private HostServicePropertiesImplFactory factory;

    @Reference
    private TypedAllPropertiesService typedAllPropertiesService;

    @Override
    public HostServiceProperties create() {
        return factory.create();
    }

    @Activate
    protected void start() {
        createInjector(new PropertiesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TypedAllPropertiesFactory.class)
                        .toProvider(of(typedAllPropertiesService));
            }
        }).injectMembers(this);
    }
}
