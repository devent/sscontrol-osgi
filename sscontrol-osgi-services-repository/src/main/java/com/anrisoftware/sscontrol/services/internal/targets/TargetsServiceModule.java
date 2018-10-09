package com.anrisoftware.sscontrol.services.internal.targets;

import com.anrisoftware.sscontrol.services.internal.cluster.ClustersImpl.ClustersImplFactory;
import com.anrisoftware.sscontrol.services.internal.registry.RegistriesImpl.RegistriesImplFactory;
import com.anrisoftware.sscontrol.services.internal.repo.ReposImpl.ReposImplFactory;
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClustersService;
import com.anrisoftware.sscontrol.types.registry.external.RegistriesService;
import com.anrisoftware.sscontrol.types.repo.external.ReposService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TargetsService.class).to(TargetsImplFactory.class);
        bind(ClustersService.class).to(ClustersImplFactory.class);
        bind(ReposService.class).to(ReposImplFactory.class);
        bind(RegistriesService.class).to(RegistriesImplFactory.class);
    }

}
