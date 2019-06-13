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
package com.anrisoftware.sscontrol.command.shell.internal.cmd;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs.SshArgsFactory;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgsService;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@Component(service = SshArgsService.class)
public class SshArgsServiceImpl implements SshArgsService {

    @Inject
    private SshArgsFactory sshArgsFactory;

    @Override
    public SshArgs create(Map<String, Object> args) {
        return sshArgsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new CmdModule()).injectMembers(this);
    }

}
