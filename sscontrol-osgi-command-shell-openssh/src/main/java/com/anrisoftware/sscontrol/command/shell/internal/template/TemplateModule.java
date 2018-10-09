package com.anrisoftware.sscontrol.command.shell.internal.template;

import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TemplateModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Template.class, TemplateImpl.class)
                .build(TemplateFactory.class));
    }

}
