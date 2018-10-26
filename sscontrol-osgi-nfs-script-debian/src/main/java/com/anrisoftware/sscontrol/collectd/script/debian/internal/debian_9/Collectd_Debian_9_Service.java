package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9;

/*-
 * #%L
 * sscontrol-osgi - collectd-script-debian
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

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component(service = HostServiceScriptService.class)
public class Collectd_Debian_9_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "9";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private Collectd_Debian_9_Factory scriptFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices rep, HostService service, TargetHost target, ExecutorService threads,
            Map<String, Object> env) {
        return scriptFactory.create(rep, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new Collectd_Debian_9_Module()).injectMembers(this);
    }

}
