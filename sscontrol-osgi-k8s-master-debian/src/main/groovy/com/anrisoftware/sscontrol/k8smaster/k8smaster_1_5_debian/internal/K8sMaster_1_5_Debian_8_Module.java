package com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal;

import com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal.K8sMaster_1_5_Debian_8.K8sMaster_1_5_Debian_8_Factory;
import com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal.K8sMaster_1_5_Systemd_Debian_8.K8sMaster_1_5_Systemd_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sMaster_1_5_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        K8sMaster_1_5_Debian_8.class)
                .build(K8sMaster_1_5_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        K8sMaster_1_5_Systemd_Debian_8.class)
                .build(K8sMaster_1_5_Systemd_Debian_8_Factory.class));
    }

}
