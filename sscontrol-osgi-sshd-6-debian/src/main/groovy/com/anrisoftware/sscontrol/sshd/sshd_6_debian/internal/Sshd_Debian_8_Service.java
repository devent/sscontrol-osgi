/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal;

import static com.google.inject.Guice.createInjector;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal.Sshd_Debian_8.Sshd_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class Sshd_Debian_8_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "8";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private Sshd_Debian_8_Factory scriptFactory;

    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, SshHost target, ExecutorService threads) {
        return scriptFactory.create(repository, service, target, threads);
    }

    @Activate
    protected void start() {
        createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                install(new FactoryModuleBuilder()
                        .implement(HostServiceScript.class, Sshd_Debian_8.class)
                        .build(Sshd_Debian_8_Factory.class));
            }
        }).injectMembers(this);
    }

}
