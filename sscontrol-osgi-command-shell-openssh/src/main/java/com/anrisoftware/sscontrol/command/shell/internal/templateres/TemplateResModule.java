package com.anrisoftware.sscontrol.command.shell.internal.templateres;

import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResourceArgs.TemplateResourceArgsFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TemplateResModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(TemplateResourceArgs.class,
                        TemplateResourceArgs.class)
                .build(TemplateResourceArgsFactory.class));
    }

}
