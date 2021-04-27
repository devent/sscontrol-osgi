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
package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.etcd.service.external.Authentication;
import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Client;
import com.anrisoftware.sscontrol.etcd.service.external.Cluster;
import com.anrisoftware.sscontrol.etcd.service.external.Gateway;
import com.anrisoftware.sscontrol.etcd.service.external.Peer;
import com.anrisoftware.sscontrol.etcd.service.external.Proxy;
import com.anrisoftware.sscontrol.etcd.service.internal.ClientCertsAuthenticationImpl.ClientCertsAuthenticationImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.EtcdImpl.EtcdImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.GatewayImpl.GatewayImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.PeerClientCertsAuthenticationImpl.PeerClientCertsAuthenticationImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.PeerImpl.PeerImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.ProxyImpl.ProxyImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Etcd</i> script module.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class EtcdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostService.class, EtcdImpl.class).build(EtcdImplFactory.class));
        install(new FactoryModuleBuilder().implement(Peer.class, PeerImpl.class).build(PeerImplFactory.class));
        install(new FactoryModuleBuilder().implement(Cluster.class, ClusterImpl.class).build(ClusterImplFactory.class));
        install(new FactoryModuleBuilder().implement(Authentication.class, ClientCertsAuthenticationImpl.class)
                .build(ClientCertsAuthenticationImplFactory.class));
        install(new FactoryModuleBuilder().implement(Authentication.class, PeerClientCertsAuthenticationImpl.class)
                .build(PeerClientCertsAuthenticationImplFactory.class));
        install(new FactoryModuleBuilder().implement(Client.class, ClientImpl.class).build(ClientImplFactory.class));
        install(new FactoryModuleBuilder().implement(Proxy.class, ProxyImpl.class).build(ProxyImplFactory.class));
        install(new FactoryModuleBuilder().implement(Gateway.class, GatewayImpl.class).build(GatewayImplFactory.class));
        bindAuthentication();
    }

    private void bindAuthentication() {
        MapBinder<String, AuthenticationFactory> mapbinder = newMapBinder(binder(), String.class,
                AuthenticationFactory.class);
        mapbinder.addBinding("cert").to(ClientCertsAuthenticationImplFactory.class);
        mapbinder.addBinding("peer-cert").to(PeerClientCertsAuthenticationImplFactory.class);
    }

}
