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
package com.anrisoftware.sscontrol.icinga.script.debian.internal.debian_9.icinga_2;

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
public class IcingaDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, IcingaDebian.class)
                .build(IcingaDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        IcingaUpstreamDebian.class)
                .build(IcingaUpstreamDebianFactory.class));
        installPlugins();
    }

    private void installPlugins() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        IdoMysqlPluginUpstreamDebian.class)
                .build(IdoMysqlPluginUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        GenericPluginUpstreamDebian.class)
                .build(GenericPluginUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        ApiPluginUpstreamDebian.class)
                .build(ApiPluginUpstreamDebianFactory.class));
        MapBinder<String, PluginHostServiceScriptService> mapbinder = newMapBinder(
                binder(), String.class, PluginHostServiceScriptService.class);
        mapbinder.addBinding("generic")
                .to(GenericPluginUpstreamDebianFactory.class);
        mapbinder.addBinding("api")
                .to(ApiPluginUpstreamDebianFactory.class);
        mapbinder.addBinding("ido-mysql")
                .to(IdoMysqlPluginUpstreamDebianFactory.class);
    }
}
