package com.anrisoftware.sscontrol.flanneldocker.service.internal;

/*-
 * #%L
 * sscontrol-osgi - flannel-docker-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerPreScriptImpl.K8sMasterPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Etcd</i> pre-script module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FlannelDockerPreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, FlannelDockerPreScriptImpl.class)
                .build(K8sMasterPreScriptImplFactory.class));
    }

}
