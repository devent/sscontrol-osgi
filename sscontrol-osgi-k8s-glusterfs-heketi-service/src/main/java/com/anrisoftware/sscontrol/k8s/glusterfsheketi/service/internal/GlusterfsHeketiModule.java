package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Admin;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Storage;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.User;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.AdminImpl.AdminImplFactory;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImpl.GlusterfsHeketiImplFactory;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.StorageImpl.StorageImplFactory;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.UserImpl.UserImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class GlusterfsHeketiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, GlusterfsHeketiImpl.class)
                .build(GlusterfsHeketiImplFactory.class));
        install(new FactoryModuleBuilder().implement(User.class, UserImpl.class)
                .build(UserImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Admin.class, AdminImpl.class)
                .build(AdminImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Storage.class, StorageImpl.class)
                .build(StorageImplFactory.class));
    }

}
