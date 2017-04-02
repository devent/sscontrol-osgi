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
package com.anrisoftware.sscontrol.k8scluster.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.k8scluster.external.Cluster;
import com.anrisoftware.sscontrol.k8scluster.external.Context;
import com.anrisoftware.sscontrol.k8scluster.external.Credentials;
import com.anrisoftware.sscontrol.k8scluster.external.CredentialsFactory;
import com.anrisoftware.sscontrol.k8scluster.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8scluster.internal.ContextImpl.ContextImplFactory;
import com.anrisoftware.sscontrol.k8scluster.internal.CredentialsCertImpl.CredentialsCertImplFactory;
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterImpl.K8sClusterImplFactory;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>K8s-Master</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sClusterModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, K8sClusterImpl.class)
                .build(K8sClusterImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Cluster.class, ClusterImpl.class)
                .build(ClusterImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Context.class, ContextImpl.class)
                .build(ContextImplFactory.class));
        bindCredentials();
    }

    private void bindCredentials() {
        install(new FactoryModuleBuilder()
                .implement(Credentials.class, CredentialsCertImpl.class)
                .build(CredentialsCertImplFactory.class));
        MapBinder<String, CredentialsFactory> mapbinder = newMapBinder(binder(),
                String.class, CredentialsFactory.class);
        mapbinder.addBinding("cert").to(CredentialsCertImplFactory.class);
    }

}
