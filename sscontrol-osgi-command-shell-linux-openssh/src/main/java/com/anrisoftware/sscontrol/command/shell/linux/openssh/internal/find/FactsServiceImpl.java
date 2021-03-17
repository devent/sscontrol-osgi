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
package com.anrisoftware.sscontrol.command.shell.linux.openssh.internal.find;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFiles;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesFactory;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Find files command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = FindFilesService.class)
public class FactsServiceImpl implements FindFilesService {

    @Inject
    private FindFilesFactory filesFactory;

    @Override
    public FindFiles create(Map<String, Object> args, SshHost ssh, Object parent, Threads threads, Object log) {
        return filesFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new FindModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
