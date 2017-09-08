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
package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import com.anrisoftware.sscontrol.types.cluster.external.Credentials;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class KubectlLinuxModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Credentials.class, CredentialsNop.class)
                .build(CredentialsNopFactory.class));
        install(new FactoryModuleBuilder()
                .implement(KubectlClient.class, KubectlClient.class)
                .build(KubectlClientFactory.class));
        install(new FactoryModuleBuilder()
                .implement(KubeNodeClient.class, KubeNodeClient.class)
                .build(KubeNodeClientFactory.class));
        install(new FactoryModuleBuilder()
                .implement(KubeNode.class, KubeNode.class)
                .build(KubeNodeFactory.class));
        install(new FactoryModuleBuilder()
                .implement(KubeNodeResource.class, KubeNodeResource.class)
                .build(KubeNodeResourceFactory.class));
    }

}
