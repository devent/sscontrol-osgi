package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FromRepositoryLinuxModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, FromRepositoryLinux.class)
                .build(FromRepositoryLinuxFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, KubectlClusterLinux.class)
                .build(KubectlClusterLinuxFactory.class));
    }

}
