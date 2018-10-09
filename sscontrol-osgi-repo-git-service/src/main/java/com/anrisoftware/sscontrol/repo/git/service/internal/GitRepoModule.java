package com.anrisoftware.sscontrol.repo.git.service.internal;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.repo.git.service.external.Checkout;
import com.anrisoftware.sscontrol.repo.git.service.external.Credentials;
import com.anrisoftware.sscontrol.repo.git.service.external.GitRepoHost;
import com.anrisoftware.sscontrol.repo.git.service.external.Remote;
import com.anrisoftware.sscontrol.repo.git.service.internal.CheckoutImpl.CheckoutImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoHostImpl.GitRepoHostImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.RemoteImpl.RemoteImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.SshCredentialsImpl.SshCredentialsImplFactory;
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
public class GitRepoModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, GitRepoImpl.class)
                .build(GitRepoImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Remote.class, RemoteImpl.class)
                .build(RemoteImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(GitRepoHost.class, GitRepoHostImpl.class)
                .build(GitRepoHostImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Checkout.class, CheckoutImpl.class)
                .build(CheckoutImplFactory.class));
        bindCredentials();
    }

    private void bindCredentials() {
        install(new FactoryModuleBuilder()
                .implement(Credentials.class, SshCredentialsImpl.class)
                .build(SshCredentialsImplFactory.class));
        MapBinder<String, CredentialsFactory> mapbinder = newMapBinder(binder(),
                String.class, CredentialsFactory.class);
        mapbinder.addBinding("ssh").to(SshCredentialsImplFactory.class);
    }

}
