package com.anrisoftware.sscontrol.properties.internal;

import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.google.inject.AbstractModule;

/**
 * Binds the {@link HostServicePropertiesServiceModule}
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostServicePropertiesServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HostServicePropertiesService.class)
                .to(HostServicePropertiesImplFactory.class);
    }

}
