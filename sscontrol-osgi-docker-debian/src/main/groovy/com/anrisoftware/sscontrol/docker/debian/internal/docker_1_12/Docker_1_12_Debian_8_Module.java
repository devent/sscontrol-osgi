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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12;

import com.anrisoftware.sscontrol.types.external.host.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Docker_1_12_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Docker_1_12_Debian_8.class)
                .build(Docker_1_12_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Docker_1_12_Systemd_Debian_8.class)
                .build(Docker_1_12_Systemd_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Docker_1_12_Upstream_Debian_8.class)
                .build(Docker_1_12_Upstream_Debian_8_Factory.class));
    }

}
