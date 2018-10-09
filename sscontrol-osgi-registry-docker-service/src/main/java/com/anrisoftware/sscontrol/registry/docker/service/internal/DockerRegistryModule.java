package com.anrisoftware.sscontrol.registry.docker.service.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials;
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryHost;
import com.anrisoftware.sscontrol.registry.docker.service.external.Host;
import com.anrisoftware.sscontrol.registry.docker.service.external.Registry;
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryHostImpl.DockerRegistryHostImplFactory;
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory;
import com.anrisoftware.sscontrol.registry.docker.service.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.registry.docker.service.internal.RegistryImpl.RegistryImplFactory;
import com.anrisoftware.sscontrol.registry.docker.service.internal.UserCredentialsImpl.UserCredentialsImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>K8s-Master</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DockerRegistryModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, DockerRegistryImpl.class)
                .build(DockerRegistryImplFactory.class));
        install(new FactoryModuleBuilder().implement(Host.class, HostImpl.class)
                .build(HostImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(DockerRegistryHost.class,
                        DockerRegistryHostImpl.class)
                .build(DockerRegistryHostImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Registry.class, RegistryImpl.class)
                .build(RegistryImplFactory.class));
        bindCredentials();
    }

    private void bindCredentials() {
        install(new FactoryModuleBuilder()
                .implement(Credentials.class, UserCredentialsImpl.class)
                .build(UserCredentialsImplFactory.class));
        MapBinder<String, CredentialsFactory> mapbinder = newMapBinder(binder(),
                String.class, CredentialsFactory.class);
        mapbinder.addBinding("user").to(UserCredentialsImplFactory.class);
    }

}
