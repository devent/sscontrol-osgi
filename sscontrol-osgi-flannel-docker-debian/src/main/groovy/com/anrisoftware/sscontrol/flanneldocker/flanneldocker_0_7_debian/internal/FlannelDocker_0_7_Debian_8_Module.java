package com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal;

import com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Debian_8.FlannelDocker_0_7_Debian_8_Factory;
import com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Systemd_Debian_8.FlannelDocker_0_7_Systemd_Debian_8_Factory;
import com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Upstream_Debian_8.FlannelDocker_0_7_Upstream_Debian_8_Factory;
import com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Upstream_Systemd_Debian_8.FlannelDocker_0_7_Upstream_Systemd_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FlannelDocker_0_7_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, FlannelDocker_0_7_Debian_8.class)
                .build(FlannelDocker_0_7_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        FlannelDocker_0_7_Systemd_Debian_8.class)
                .build(FlannelDocker_0_7_Systemd_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        FlannelDocker_0_7_Upstream_Systemd_Debian_8.class)
                .build(FlannelDocker_0_7_Upstream_Systemd_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        FlannelDocker_0_7_Upstream_Debian_8.class)
                .build(FlannelDocker_0_7_Upstream_Debian_8_Factory.class));
    }

}
