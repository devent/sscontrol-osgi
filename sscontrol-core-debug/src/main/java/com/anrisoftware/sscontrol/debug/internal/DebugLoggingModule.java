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
package com.anrisoftware.sscontrol.debug.internal;

import com.anrisoftware.sscontrol.debug.external.DebugLoggingService;
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingImpl.DebugLoggingImplFactory;
import com.anrisoftware.sscontrol.debug.internal.DebugModuleImpl.DebugModuleImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DebugLoggingModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(DebugLoggingImpl.class, DebugLoggingImpl.class)
				.build(DebugLoggingImplFactory.class));
		install(new FactoryModuleBuilder().implement(DebugModuleImpl.class, DebugModuleImpl.class)
				.build(DebugModuleImplFactory.class));
        bind(DebugLoggingService.class).to(DebugLoggingServiceImpl.class);
	}

}