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
package com.anrisoftware.sscontrol.command.shell.external.ssh;

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.PRIVILEGED_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SHELL_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_CONTROL_PATH_ARG;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs.CmdArgsFactory;

/**
 * Setups the SSH master socket.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public abstract class AbstractCmdRun {

    protected final Object parent;

    protected final Threads threads;

    protected final Map<String, Object> argsMap;

    protected SshArgs args;

    @Inject
    protected ScriptExecFactory scriptEx;

    @Inject
    private AbstractCmdRunLogger log;

    protected AbstractCmdRun(Map<String, Object> args, Object parent, Threads threads) {
        this.argsMap = new HashMap<String, Object>(args);
        this.parent = parent;
        this.threads = threads;
    }

    @Inject
    void createArgs(CmdArgsFactory argsFactory) {
        this.args = argsFactory.create(argsMap).getArgs();
    }

    /**
     * Executes the command.
     */
    public abstract ProcessTask call() throws CommandExecException;

    /**
     * Creates the master socket directory.
     */
    protected void createSocketDir(Map<String, Object> args) {
        if (!args.containsKey(SSH_CONTROL_PATH_ARG)) {
            return;
        }
        String path = args.get(SSH_CONTROL_PATH_ARG).toString();
        File dir = new File(path).getParentFile();
        if (dir == null) {
            return;
        }
        if (!dir.isDirectory()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new ControlPathCreateDirErrorException(dir);
            }
        }
        dir.setWritable(true, true);
    }

    protected String getShellName(Map<String, Object> args) {
        String shell = args.get(SHELL_ARG).toString();
        shell = StringUtils.split(shell)[0];
        return getBaseName(shell);
    }

    protected ProcessTask runCommand(TemplateResource res, Map<String, Object> args) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    protected Boolean isPrivileged(Map<String, Object> args) {
        Boolean privileged = (Boolean) args.get(PRIVILEGED_ARG);
        return privileged != null && privileged;
    }

    protected static final String COMMAND_ARG = "command";

}
