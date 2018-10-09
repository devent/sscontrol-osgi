package com.anrisoftware.sscontrol.utils.debian.external;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DebianUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(DebianUtils.class, Debian_9_Utils.class)
                .build(Debian_9_UtilsFactory.class));
    }

}
