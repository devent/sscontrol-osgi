package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_12;

import com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_12.FromHelmLinux;
import com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_12.KubectlClusterLinux;

/*-
 * #%L
 * sscontrol-osgi - k8s-from-helm-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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