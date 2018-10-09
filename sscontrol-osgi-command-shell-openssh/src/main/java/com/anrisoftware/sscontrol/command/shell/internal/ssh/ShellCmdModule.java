package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import com.anrisoftware.sscontrol.command.shell.external.Shell;
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ShellCmdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Shell.class, ShellImpl.class)
                .build(ShellFactory.class));
    }

}
