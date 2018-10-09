package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshdDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, SshdDebian.class)
                .build(SshdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, SshdDebianUfw.class)
                .build(SshdDebianUfwFactory.class));
    }

}
