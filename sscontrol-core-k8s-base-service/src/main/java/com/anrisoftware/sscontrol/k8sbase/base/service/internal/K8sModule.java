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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Label;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.LabelFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.TaintFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.AddonManagerPluginImpl.AddonManagerPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.CanalPluginImpl.CanalPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.EtcdPluginImpl.EtcdPluginImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImpl.K8sImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImpl.KubeletImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.NfsClientPluginImpl.NfsClientPluginImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>K8s</i> script module.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class K8sModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostService.class, K8sImpl.class).build(K8sImplFactory.class));
        install(new FactoryModuleBuilder().implement(Cluster.class, ClusterImpl.class).build(ClusterImplFactory.class));
        install(new FactoryModuleBuilder().implement(Kubelet.class, KubeletImpl.class).build(KubeletImplFactory.class));
        install(new FactoryModuleBuilder().implement(Label.class, LabelImpl.class).build(LabelFactory.class));
        install(new FactoryModuleBuilder().implement(Taint.class, TaintImpl.class).build(TaintFactory.class));
        bindPlugins();
    }

    private void bindPlugins() {
        install(new FactoryModuleBuilder().implement(Plugin.class, EtcdPluginImpl.class)
                .build(EtcdPluginImplFactory.class));
        install(new FactoryModuleBuilder().implement(Plugin.class, CanalPluginImpl.class)
                .build(CanalPluginImplFactory.class));
        install(new FactoryModuleBuilder().implement(Plugin.class, AddonManagerPluginImpl.class)
                .build(AddonManagerPluginImplFactory.class));
        install(new FactoryModuleBuilder().implement(Plugin.class, NfsClientPluginImpl.class)
                .build(NfsClientPluginImplFactory.class));
        MapBinder<String, PluginFactory> mapbinder = newMapBinder(binder(), String.class, PluginFactory.class);
        mapbinder.addBinding("etcd").to(EtcdPluginImplFactory.class);
        mapbinder.addBinding("canal").to(CanalPluginImplFactory.class);
        mapbinder.addBinding("addon-manager").to(AddonManagerPluginImplFactory.class);
        mapbinder.addBinding("nfs-client").to(NfsClientPluginImplFactory.class);
    }

}
