package com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal;

import com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal.Etcd_3_1_Debian_8.Etcd_3_1_Debian_8_Factory;
import com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal.Etcd_3_1_Systemd_Debian_8.Etcd_3_1_Systemd_Debian_8_Factory;
import com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal.Etcd_3_1_Upstream_Debian_8.Etcd_3_1_Upstream_Debian_8_Factory;
import com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal.Etcd_3_1_Upstream_Systemd_Debian_8.Etcd_3_1_Upstream_Systemd_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Etcd_3_1_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Etcd_3_1_Debian_8.class)
                .build(Etcd_3_1_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Etcd_3_1_Systemd_Debian_8.class)
                .build(Etcd_3_1_Systemd_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Etcd_3_1_Upstream_Systemd_Debian_8.class)
                .build(Etcd_3_1_Upstream_Systemd_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Etcd_3_1_Upstream_Debian_8.class)
                .build(Etcd_3_1_Upstream_Debian_8_Factory.class));
    }

}
