package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sMasterDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sMasterDebian.class)
                .build(K8sMasterDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        K8sMasterUpstreamDebian.class)
                .build(K8sMasterUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, KubectlClusterDebian.class)
                .build(KubectlClusterDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sMasterUfwDebian.class)
                .build(K8sMasterUfwDebianFactory.class));
    }

}
