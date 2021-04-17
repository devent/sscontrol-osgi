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
package com.anrisoftware.sscontrol.command.shell.internal.scp;

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_EXTERNAL_CONTROL_PATH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_USER_ARG;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.Scp;
import com.anrisoftware.sscontrol.command.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ScpImpl implements Scp {

    private static final String DEST_ARG = "dest";

    private static final String SRC_ARG = "src";

    private static final String LOG_ARG = "log";

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    @Inject
    private ScpRunFactory scpRunFactory;

    @Inject
    ScpImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        setupArgs();
        checkArgs(this.args);
    }

    @Override
    public ProcessTask call() throws AppException {
        try {
            return scpRunFactory.create(args, parent, threads).call();
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "scp");
        }
    }

    private void checkArgs(Map<String, Object> args) {
        isTrue(args.containsKey(SRC_ARG), "%s=null", SRC_ARG);
        notNull(args.get(SRC_ARG), "%s=null", SRC_ARG);
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupArgs() {
        args.put(LOG_ARG, log);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
        args.put(SSH_EXTERNAL_CONTROL_PATH_ARG, host.getSocket());
    }

}
