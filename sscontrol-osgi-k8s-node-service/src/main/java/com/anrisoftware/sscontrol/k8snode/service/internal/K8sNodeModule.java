package com.anrisoftware.sscontrol.k8snode.service.internal;

import com.anrisoftware.sscontrol.k8snode.service.internal.K8sNodeImpl.K8sNodeImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>K8s-Node</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sNodeModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, K8sNodeImpl.class)
                .build(K8sNodeImplFactory.class));
    }

}
