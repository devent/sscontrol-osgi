package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal;

import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Fail2ban_0_8_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Fail2ban_0_8_Debian_8.class)
                .build(Fail2ban_0_8_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Ufw_Fail2ban_0_8_Debian_8.class)
                .build(Ufw_Fail2ban_0_8_Debian_8_Factory.class));
    }

}
