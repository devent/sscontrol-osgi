/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class EtcdDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdDebian.class)
                .build(EtcdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        EtcdUpstreamSystemdDebian.class)
                .build(EtcdUpstreamSystemdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdUpstreamDebian.class)
                .build(EtcdUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdDefaults.class)
                .build(EtcdDefaultsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdUfw.class)
                .build(EtcdUfwFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdVirtualInterface.class)
                .build(EtcdVirtualInterfaceFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, EtcdCheck.class)
                .build(EtcdCheckFactory.class));
    }

}
