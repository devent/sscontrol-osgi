/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromhelm.service.internal;

import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.Release;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImpl.FromHelmImplFactory;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.ReleaseImpl.ReleaseImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
