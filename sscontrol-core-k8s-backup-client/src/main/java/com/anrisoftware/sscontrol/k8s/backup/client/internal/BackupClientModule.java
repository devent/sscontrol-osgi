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
package com.anrisoftware.sscontrol.k8s.backup.client.internal;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory;
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient;
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClientFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class BackupClientModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Deployment.class, DeploymentImpl.class)
                .build(DeploymentFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RsyncClient.class, RsyncClientImpl.class)
                .build(RsyncClientFactory.class));
    }

}
