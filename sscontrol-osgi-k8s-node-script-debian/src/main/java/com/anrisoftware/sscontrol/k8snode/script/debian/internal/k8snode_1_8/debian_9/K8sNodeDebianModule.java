package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sNodeDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeDebian.class)
                .build(K8sNodeDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeUpstreamDebian.class)
                .build(K8sNodeUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, KubectlClusterDebian.class)
                .build(KubectlClusterDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeUfwDebian.class)
                .build(K8sNodeUfwDebianFactory.class));
    }

}
