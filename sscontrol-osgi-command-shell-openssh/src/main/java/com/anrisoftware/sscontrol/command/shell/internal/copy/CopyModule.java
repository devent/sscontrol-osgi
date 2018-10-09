package com.anrisoftware.sscontrol.command.shell.internal.copy;

import com.anrisoftware.sscontrol.command.copy.external.Copy;
import com.anrisoftware.sscontrol.command.copy.external.Copy.CopyFactory;
import com.anrisoftware.sscontrol.command.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.copy.ScpCopyWorker.ScpCopyWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CopyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Copy.class, CopyImpl.class)
                .build(CopyFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Copy.class, ScpCopyWorker.class)
                .build(ScpCopyWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Copy.class, DownloadCopyWorker.class)
                .build(DownloadCopyWorkerFactory.class));
    }

}
