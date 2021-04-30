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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class Etcd_3_4_Debian_10_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Etcd_3_4_Debian_10_Debian.class)
                .build(Etcd_3_4_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, SystemdUpstreamEtcd_3_4_Debian_10_Debian.class)
                .build(SystemdUpstreamEtcd_3_4_Debian_10_DebianFactory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, UpstreamEtcd_3_4_Debian_10_Debian.class)
                .build(UpstreamEtcd_3_4_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Etcd_3_4_Debian_10_Defaults.class)
                .build(Etcd_3_4_Debian_10_DefaultsFactory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, UfwEtcd_3_4_Debian_10.class)
                .build(UfwEtcd_3_4_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, VirtualInterfaceEtcd_3_4_Debian_10_Debian.class)
                .build(VirtualInterfaceEtcd_3_4_Debian_10_Factory.class));
        install(new FactoryModuleBuilder().implement(HostServiceScript.class, Etcd_3_4_Debian_10_Check.class)
                .build(Etcd_3_4_Debian_10_CheckFactory.class));
    }

}
