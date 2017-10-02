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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9;

import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.K8sNodeDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.K8sNodeDockerDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.K8sNodeSystemdDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.K8sNodeUfwDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.K8sNodeUpstreamDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.KubectlClusterDebian;
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9.KubectlUpstreamDebian;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sNodeDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeDebian.class)
                .build(K8sNodeDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeUpstreamDebian.class)
                .build(K8sNodeUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, KubectlUpstreamDebian.class)
                .build(KubectlUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, KubectlClusterDebian.class)
                .build(KubectlClusterDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeSystemdDebian.class)
                .build(K8sNodeSystemdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeDockerDebian.class)
                .build(K8sNodeDockerDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, K8sNodeUfwDebian.class)
                .build(K8sNodeUfwDebianFactory.class));
    }

}
