package com.anrisoftware.sscontrol.command.shell.internal.facts;

import com.anrisoftware.sscontrol.command.facts.external.Facts;
import com.anrisoftware.sscontrol.command.facts.external.Facts.FactsFactory;
import com.anrisoftware.sscontrol.command.shell.internal.facts.CatReleaseParse.CatReleaseParseFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FactsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Facts.class, FactsImpl.class)
                .build(FactsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CatReleaseParse.class, CatReleaseParse.class)
                .build(CatReleaseParseFactory.class));
    }

}
