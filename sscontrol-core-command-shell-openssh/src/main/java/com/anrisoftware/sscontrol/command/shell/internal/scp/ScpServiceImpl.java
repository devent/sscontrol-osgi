/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.command.shell.internal.scp;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.Scp;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.command.shell.external.ScpService;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgsService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Scp command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = ScpService.class)
public class ScpServiceImpl implements ScpService {

    @Inject
    private ScpFactory scpFactory;

    @Reference
    private CmdArgsService cmdArgsService;

    @Override
    public Scp create(Map<String, Object> args, SshHost ssh, Object parent, Threads threads, Object log) {
        return scpFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new ScpModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(CmdArgsService.class).toProvider(of(cmdArgsService));
            }
        }).injectMembers(this);
    }
}
