package com.anrisoftware.sscontrol.command.shell.internal.replace;

import com.anrisoftware.sscontrol.command.replace.external.Replace;
import com.anrisoftware.sscontrol.command.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.CreateTempFileWorker.CreateTempFileWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.ParseSedSyntax.ParseSedSyntaxFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceLine.ReplaceLineFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceWorker.ReplaceWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ReplaceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Replace.class, ReplaceImpl.class)
                .build(ReplaceFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ReplaceWorker.class, ReplaceWorker.class)
                .build(ReplaceWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(LoadFileWorker.class, LoadFileWorker.class)
                .build(LoadFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(PushFileWorker.class, PushFileWorker.class)
                .build(PushFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CreateTempFileWorker.class,
                        CreateTempFileWorker.class)
                .build(CreateTempFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ParseSedSyntax.class, ParseSedSyntax.class)
                .build(ParseSedSyntaxFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ReplaceLine.class, ReplaceLine.class)
                .build(ReplaceLineFactory.class));
    }

}
