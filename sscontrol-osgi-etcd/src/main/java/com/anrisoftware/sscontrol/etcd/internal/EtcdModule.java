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
package com.anrisoftware.sscontrol.etcd.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.etcd.external.Authentication;
import com.anrisoftware.sscontrol.etcd.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.etcd.internal.ClientCertsAuthenticationImpl.ClientCertsAuthenticationImplFactory;
import com.anrisoftware.sscontrol.etcd.internal.EtcdImpl.EtcdImplFactory;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Etcd</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class EtcdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, EtcdImpl.class)
                .build(EtcdImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Authentication.class,
                        ClientCertsAuthenticationImpl.class)
                .build(ClientCertsAuthenticationImplFactory.class));
        bindAuthentication();
    }

    private void bindAuthentication() {
        MapBinder<String, AuthenticationFactory> mapbinder = newMapBinder(
                binder(), String.class, AuthenticationFactory.class);
        mapbinder.addBinding("cert")
                .to(ClientCertsAuthenticationImplFactory.class);
    }

}
