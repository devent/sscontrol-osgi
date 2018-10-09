package com.anrisoftware.sscontrol.sshd.service.internal;

import com.anrisoftware.sscontrol.sshd.service.external.Binding;
import com.anrisoftware.sscontrol.sshd.service.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.sshd.service.internal.SshdImpl.SshdImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class SshdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, SshdImpl.class)
                .build(SshdImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Binding.class, BindingImpl.class)
                .build(BindingImplFactory.class));
    }

}
