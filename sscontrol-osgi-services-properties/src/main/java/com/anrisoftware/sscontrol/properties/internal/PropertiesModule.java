package com.anrisoftware.sscontrol.properties.internal;

import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @see HostServicePropertiesImpl
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceProperties.class,
                        HostServicePropertiesImpl.class)
                .build(HostServicePropertiesImplFactory.class));
    }

}
