package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerPreScriptImpl.K8sMasterPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Etcd</i> pre-script module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FlannelDockerPreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, FlannelDockerPreScriptImpl.class)
                .build(K8sMasterPreScriptImplFactory.class));
    }

}
