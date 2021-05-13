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
package com.anrisoftware.sscontrol.crio.script.debian.debian_10;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * CRI-O 1.20 Debian 10 module.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class Crio_1_20_Debian_10_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Crio_1_20_Debian_10.class)
                .build(Crio_1_20_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Upstream_Crio_Debian_10.class)
                .build(Upstream_Crio_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Crio_Crio_1_20_Debian_10.class)
                .build(Crio_Crio_1_20_Debian_10_Factory.class));
    }

}
