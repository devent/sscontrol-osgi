package com.anrisoftware.sscontrol.shell.internal;

import com.anrisoftware.sscontrol.shell.external.Script;
import com.anrisoftware.sscontrol.shell.internal.ScriptImpl.ScriptImplFactory;
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Shell service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ShellModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, ShellImpl.class)
                .build(ShellImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Script.class, ScriptImpl.class)
                .build(ScriptImplFactory.class));
    }

}
