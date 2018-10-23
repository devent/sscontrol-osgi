package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_12.linux;

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_12.linux.Addresses;
import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_12.linux.PluginTargetsMap;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sUpstreamModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PluginTargetsMap.class, PluginTargetsMap.class)
                .build(PluginTargetsMapFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Addresses.class, Addresses.class)
                .build(AddressesFactory.class));
    }

}
