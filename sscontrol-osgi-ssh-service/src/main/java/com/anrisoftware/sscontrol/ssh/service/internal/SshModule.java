package com.anrisoftware.sscontrol.ssh.service.internal;

import com.anrisoftware.sscontrol.ssh.service.internal.SshHostImpl.SshHostImplFactory;
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Ssh</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, SshImpl.class)
                .build(SshImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SshHost.class, SshHostImpl.class)
                .build(SshHostImplFactory.class));
    }

}
