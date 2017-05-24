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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Icinga_2_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Icinga_2_Debian_8.class)
                .build(Icinga_2_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Icinga_2_Upstream_Debian_8.class)
                .build(Icinga_2_Upstream_Debian_8_Factory.class));
        installPlugins();
    }

    private void installPlugins() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        IdoMysqlPlugin_Upstream_Debian_8.class)
                .build(IdoMysqlPlugin_Upstream_Debian_8_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        GenericPlugin_Upstream_Debian_8.class)
                .build(GenericPlugin_Upstream_Debian_8_Factory.class));
        MapBinder<String, PluginHostServiceScriptService> mapbinder = newMapBinder(
                binder(), String.class, PluginHostServiceScriptService.class);
        mapbinder.addBinding("generic")
                .to(GenericPlugin_Upstream_Debian_8_Factory.class);
        mapbinder.addBinding("ido-mysql")
                .to(IdoMysqlPlugin_Upstream_Debian_8_Factory.class);
    }
}
