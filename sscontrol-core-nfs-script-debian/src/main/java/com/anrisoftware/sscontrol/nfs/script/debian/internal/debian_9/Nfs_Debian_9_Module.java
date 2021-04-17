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
package com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class Nfs_Debian_9_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Nfs_1_3_Impl.class)
                .build(Nfs_1_3_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Nfs_1_3_Debian_9.class)
                .build(Nfs_1_3_Debian_9_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Nfs_1_3_Ufw_Impl.class)
                .build(Nfs_1_3_Ufw_Factory.class));
    }

}
