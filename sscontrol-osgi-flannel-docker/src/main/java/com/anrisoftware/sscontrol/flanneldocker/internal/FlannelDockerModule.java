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
package com.anrisoftware.sscontrol.flanneldocker.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.flanneldocker.external.Backend;
import com.anrisoftware.sscontrol.flanneldocker.external.Backend.BackendFactory;
import com.anrisoftware.sscontrol.flanneldocker.external.Binding;
import com.anrisoftware.sscontrol.flanneldocker.external.Etcd;
import com.anrisoftware.sscontrol.flanneldocker.external.Network;
import com.anrisoftware.sscontrol.flanneldocker.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.internal.EtcdImpl.EtcdImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImpl.FlannelDockerImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.internal.NetworkImpl.NetworkImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.internal.VxlanBackendImpl.VxlanBackendImplFactory;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Flannel-Docker</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FlannelDockerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, FlannelDockerImpl.class)
                .build(FlannelDockerImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Binding.class, BindingImpl.class)
                .build(BindingImplFactory.class));
        install(new FactoryModuleBuilder().implement(Etcd.class, EtcdImpl.class)
                .build(EtcdImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Network.class, NetworkImpl.class)
                .build(NetworkImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Backend.class, VxlanBackendImpl.class)
                .build(VxlanBackendImplFactory.class));
        bindBackends();
    }

    private void bindBackends() {
        MapBinder<String, BackendFactory> mapbinder = newMapBinder(binder(),
                String.class, BackendFactory.class);
        mapbinder.addBinding("vxlan").to(VxlanBackendImplFactory.class);
    }

}
