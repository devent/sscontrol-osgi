package com.anrisoftware.sscontrol.parser.groovy.internal;

import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ParserModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(ParserImpl.class, ParserImpl.class)
                .build(ParserImplFactory.class));
    }

}
