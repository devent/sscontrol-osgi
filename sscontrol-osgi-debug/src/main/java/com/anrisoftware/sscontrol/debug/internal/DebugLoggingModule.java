package com.anrisoftware.sscontrol.debug.internal;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingImpl.DebugLoggingImplFactory;
import com.anrisoftware.sscontrol.debug.internal.DebugModuleImpl.DebugModuleImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DebugLoggingModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(DebugLoggingImpl.class,
                DebugLoggingImpl.class).build(DebugLoggingImplFactory.class));
        install(new FactoryModuleBuilder().implement(DebugModuleImpl.class,
                DebugModuleImpl.class).build(DebugModuleImplFactory.class));
        bind(DebugService.class).to(DebugServiceImpl.class);
    }

}
