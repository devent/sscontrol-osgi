package com.anrisoftware.sscontrol.command.shell.internal.st;

import com.anrisoftware.sscontrol.command.shell.internal.st.StTemplate.StTemplateFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class StModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(StTemplate.class, StTemplate.class)
                .build(StTemplateFactory.class));
    }

}
