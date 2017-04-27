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
package com.anrisoftware.sscontrol.ssh.linux.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.ssh.linux.external.Ssh_Linux_Factory;
import com.anrisoftware.sscontrol.types.external.host.HostService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.host.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.host.HostServices;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class Ssh_Linux_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "0";

    static final String SYSTEM_NAME = "linux";

    @Inject
    private Ssh_Linux_Factory sshFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, SshHost target, ExecutorService threads,
            Map<String, Object> env) {
        return sshFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new Ssh_Linux_Module()).injectMembers(this);
    }

}
