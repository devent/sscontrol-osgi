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
package com.anrisoftware.sscontrol.repo.git.linux.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class GitRepo_Linux_Service implements HostServiceScriptService {

    static final String SERVICE_NAME = "git";

    @Property(value = SERVICE_NAME)
    static final String SERVICE_NAME_PROPERTY = "service.name";

    static final String SYSTEM_VERSION = "0";

    @Property(value = SYSTEM_VERSION)
    static final String SERVICE_SYSTEM_VERSION_PROPERTY = "service.system.version";

    static final String SYSTEM_NAME = "linux";

    @Property(value = SYSTEM_NAME)
    static final String SERVICE_SYSTEM_NAME_PROPERTY = "service.system.name";

    static final String SYSTEM_SYSTEM = "linux";

    @Property(value = SYSTEM_SYSTEM)
    static final String SERVICE_SYSTEM_SYSTEM_PROPERTY = "service.system.system";

    @Inject
    private GitRepo_Linux_Factory scriptFactory;

    public ScriptInfo getSystem() {
        return new AbstractScriptInfo() {

            @Override
            public String getVersion() {
                return SYSTEM_VERSION;
            }

            @Override
            public String getSystem() {
                return SYSTEM_SYSTEM;
            }

            @Override
            public String getName() {
                return SYSTEM_NAME;
            }

            @Override
            public String getService() {
                return SERVICE_NAME;
            }
        };
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, TargetHost target, ExecutorService threads,
            Map<String, Object> env) {
        return scriptFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new GitRepo_Linux_Module()).injectMembers(this);
    }

}
