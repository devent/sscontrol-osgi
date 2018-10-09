package com.anrisoftware.sscontrol.k8s.fromhelm.service.internal;

import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.Release;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImpl.FromHelmImplFactory;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.ReleaseImpl.ReleaseImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FromHelmModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(HostService.class, FromHelmImpl.class)
				.build(FromHelmImplFactory.class));
		install(new FactoryModuleBuilder().implement(Release.class, ReleaseImpl.class).build(ReleaseImplFactory.class));
	}

}
