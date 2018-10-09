package com.anrisoftware.sscontrol.utils.centos.external;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CentosUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(CentosUtils.class, Centos_7_Utils.class)
                .build(Centos_7_UtilsFactory.class));
    }

}
