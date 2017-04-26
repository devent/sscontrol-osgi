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
package com.anrisoftware.sscontrol.services.internal.targets;

import com.anrisoftware.sscontrol.services.internal.cluster.ClustersImpl;
import com.anrisoftware.sscontrol.services.internal.cluster.ClustersImpl.ClustersImplFactory;
import com.anrisoftware.sscontrol.services.internal.repo.ReposImpl;
import com.anrisoftware.sscontrol.services.internal.repo.ReposImpl.ReposImplFactory;
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl;
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory;
import com.anrisoftware.sscontrol.types.external.cluster.Clusters;
import com.anrisoftware.sscontrol.types.external.repo.Repos;
import com.anrisoftware.sscontrol.types.external.ssh.Targets;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Targets.class, TargetsImpl.class)
                .build(TargetsImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Clusters.class, ClustersImpl.class)
                .build(ClustersImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Repos.class, ReposImpl.class)
                .build(ReposImplFactory.class));
    }

}