package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FromHelmLinuxModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(HostServiceScript.class, FromHelmLinux.class)
				.build(FromHelmLinuxFactory.class));
		install(new FactoryModuleBuilder().implement(HostServiceScript.class, KubectlClusterLinux.class)
				.build(KubectlClusterLinuxFactory.class));
	}

}
