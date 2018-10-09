package com.anrisoftware.sscontrol.command.shell.internal.fetch;

import com.anrisoftware.sscontrol.command.fetch.external.Fetch;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FetchModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Fetch.class, FetchImpl.class)
                .build(FetchFactory.class));
        install(new FactoryModuleBuilder().implement(ScpRun.class, ScpRun.class)
                .build(ScpRunFactory.class));
    }

}
