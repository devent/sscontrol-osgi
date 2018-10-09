package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Fail2ban_Debian_9_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Fail2ban_Debian_9.class)
                .build(Fail2ban_Debian_9_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Ufw_Fail2ban_Debian_9.class)
                .build(Ufw_Fail2ban_Debian_9_Factory.class));
    }

}
