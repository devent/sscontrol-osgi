package com.anrisoftware.sscontrol.flanneldocker.service.internal;

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
