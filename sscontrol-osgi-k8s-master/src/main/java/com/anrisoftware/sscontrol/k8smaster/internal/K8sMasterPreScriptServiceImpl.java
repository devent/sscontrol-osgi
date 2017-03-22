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
package com.anrisoftware.sscontrol.k8smaster.internal;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.k8sbase.base.external.K8sPreScriptService;
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sPreModule;
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sPreScriptImpl.K8sMasterPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * <i>Ssh</i> pre-script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(K8sPreScriptService.class)
public class K8sMasterPreScriptServiceImpl implements K8sPreScriptService {

    @Inject
    private K8sMasterPreScriptImplFactory sshPreScriptFactory;

    @Override
    public PreHost create() {
        return sshPreScriptFactory.create();
    }

    @Activate
    protected void start() {
        Guice.createInjector(new K8sPreModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
