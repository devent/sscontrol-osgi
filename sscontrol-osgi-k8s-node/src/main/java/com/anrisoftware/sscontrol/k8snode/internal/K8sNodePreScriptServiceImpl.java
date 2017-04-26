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
package com.anrisoftware.sscontrol.k8snode.internal;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.k8snode.external.K8sNodePreScriptService;
import com.anrisoftware.sscontrol.k8snode.internal.K8sNodePreScriptImpl.K8sNodePreScriptImplFactory;
import com.anrisoftware.sscontrol.types.external.host.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * <i>K8s-Node</i> pre-script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(K8sNodePreScriptService.class)
public class K8sNodePreScriptServiceImpl implements K8sNodePreScriptService {

    @Inject
    private K8sNodePreScriptImplFactory sshPreScriptFactory;

    @Override
    public PreHost create() {
        return sshPreScriptFactory.create();
    }

    @Activate
    protected void start() {
        Guice.createInjector(new K8sNodePreModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
