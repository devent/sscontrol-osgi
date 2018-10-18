package com.anrisoftware.sscontrol.flanneldocker.service.internal;

/*-
 * #%L
 * sscontrol-osgi - flannel-docker-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Binding;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Etcd;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Network;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend.BackendFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImpl.EtcdImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImpl.FlannelDockerImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.NetworkImpl.NetworkImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.VxlanBackendImpl.VxlanBackendImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
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
