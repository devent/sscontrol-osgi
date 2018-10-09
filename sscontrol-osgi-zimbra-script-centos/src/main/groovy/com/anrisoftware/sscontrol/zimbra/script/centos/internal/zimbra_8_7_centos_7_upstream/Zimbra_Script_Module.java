package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Zimbra_Script_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Zimbra_Script.class)
                .build(Zimbra_Script_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Zimbra_Upstream.class)
                .build(Zimbra_Upstream_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, ZimbraLetsEncrypt.class)
                .build(ZimbraLetsEncryptFactory.class));
    }

}
