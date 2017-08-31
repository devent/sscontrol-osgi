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
package com.anrisoftware.sscontrol.icinga.service.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.icinga.service.external.Config;
import com.anrisoftware.sscontrol.icinga.service.external.Database;
import com.anrisoftware.sscontrol.icinga.service.external.Feature;
import com.anrisoftware.sscontrol.icinga.service.external.Plugin;
import com.anrisoftware.sscontrol.icinga.service.internal.ConfigImpl.ConfigImplFactory;
import com.anrisoftware.sscontrol.icinga.service.internal.DatabaseImpl.DatabaseImplFactory;
import com.anrisoftware.sscontrol.icinga.service.internal.FeatureImpl.FeatureImplFactory;
import com.anrisoftware.sscontrol.icinga.service.internal.GenericPluginImpl.GenericPluginImplFactory;
import com.anrisoftware.sscontrol.icinga.service.internal.IcingaImpl.IcingaImplFactory;
import com.anrisoftware.sscontrol.icinga.service.internal.IdoMysqlPluginImpl.IdoMysqlPluginImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * Icinga script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class IcingaModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, IcingaImpl.class)
                .build(IcingaImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Database.class, DatabaseImpl.class)
                .build(DatabaseImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Feature.class, FeatureImpl.class)
                .build(FeatureImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Config.class, ConfigImpl.class)
                .build(ConfigImplFactory.class));
        installPlugins();
    }

    private void installPlugins() {
        install(new FactoryModuleBuilder()
                .implement(Plugin.class, GenericPluginImpl.class)
                .build(GenericPluginImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Plugin.class, IdoMysqlPluginImpl.class)
                .build(IdoMysqlPluginImplFactory.class));
        MapBinder<String, PluginFactory> mapbinder = newMapBinder(binder(),
                String.class, PluginFactory.class);
        mapbinder.addBinding("generic").to(IdoMysqlPluginImplFactory.class);
        mapbinder.addBinding("ido-mysql").to(IdoMysqlPluginImplFactory.class);
    }
}
