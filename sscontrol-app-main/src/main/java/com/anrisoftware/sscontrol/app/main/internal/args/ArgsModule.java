package com.anrisoftware.sscontrol.app.main.internal.args;

import com.anrisoftware.sscontrol.app.main.internal.args.AppArgs.AppArgsFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ArgsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(AppArgs.class, AppArgs.class)
                .build(AppArgsFactory.class));
    }

}
