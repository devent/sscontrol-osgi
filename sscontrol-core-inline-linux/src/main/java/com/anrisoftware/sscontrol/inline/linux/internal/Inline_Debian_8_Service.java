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
package com.anrisoftware.sscontrol.inline.linux.internal;

import static com.google.inject.Guice.createInjector;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.inline.linux.external.Inline_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.SshHost;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class Inline_Debian_8_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "8";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private Inline_Debian_8_Factory scriptFactory;

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
        createInjector(new Inline_Debian_8_Module()).injectMembers(this);
    }

}
