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
package com.anrisoftware.sscontrol.k8sbase.base.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.k8sbase.base.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.CalicoPluginImpl.CalicoPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.EtcdPluginImpl.EtcdPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.FlannelPluginImpl.FlannelPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sImpl.K8sImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.KubeletImpl.KubeletImplFactory;
import com.anrisoftware.sscontrol.types.external.host.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>K8s</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, K8sImpl.class)
                .build(K8sImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Cluster.class, ClusterImpl.class)
                .build(ClusterImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Kubelet.class, KubeletImpl.class)
                .build(KubeletImplFactory.class));
        bindPlugins();
    }

    private void bindPlugins() {
        install(new FactoryModuleBuilder()
                .implement(Plugin.class, EtcdPluginImpl.class)
                .build(EtcdPluginImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Plugin.class, FlannelPluginImpl.class)
                .build(FlannelPluginImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Plugin.class, CalicoPluginImpl.class)
                .build(CalicoPluginImplFactory.class));
        MapBinder<String, PluginFactory> mapbinder = newMapBinder(binder(),
                String.class, PluginFactory.class);
        mapbinder.addBinding("etcd").to(EtcdPluginImplFactory.class);
        mapbinder.addBinding("flannel").to(FlannelPluginImplFactory.class);
        mapbinder.addBinding("calico").to(CalicoPluginImplFactory.class);
    }

}
