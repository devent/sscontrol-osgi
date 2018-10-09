package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import com.anrisoftware.sscontrol.command.shell.external.Cmd;
import com.google.inject.AbstractModule;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CmdImplModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Cmd.class).to(CmdImpl.class);
    }

}
