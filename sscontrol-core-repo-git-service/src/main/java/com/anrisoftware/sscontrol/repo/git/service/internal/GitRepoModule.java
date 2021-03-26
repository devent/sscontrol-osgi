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
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
