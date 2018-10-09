package com.anrisoftware.sscontrol.types.groovy.internal;

import com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImpl.BindingHostImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class GroovyTypesModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(BindingHostImpl.class,
                BindingHostImpl.class).build(BindingHostImplFactory.class));
    }

}
