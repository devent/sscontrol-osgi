package com.anrisoftware.sscontrol.command.shell.internal.scp;

import com.anrisoftware.sscontrol.command.shell.external.Scp;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.CopyWorker.CopyWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.FetchWorker.FetchWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ScpModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Scp.class, ScpImpl.class)
                .build(ScpFactory.class));
        install(new FactoryModuleBuilder().implement(ScpRun.class, ScpRun.class)
                .build(ScpRunFactory.class));
        install(new FactoryModuleBuilder()
                .implement(FetchWorker.class, FetchWorker.class)
                .build(FetchWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CopyWorker.class, CopyWorker.class)
                .build(CopyWorkerFactory.class));
    }
}
