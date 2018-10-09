package com.anrisoftware.sscontrol.command.shell.linux.openssh.internal.find;

import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFiles;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FindModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(FindFiles.class, FindFilesImpl.class)
                .build(FindFilesFactory.class));
    }

}
