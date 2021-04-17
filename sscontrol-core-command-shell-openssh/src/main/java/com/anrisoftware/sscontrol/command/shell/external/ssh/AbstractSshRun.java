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

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_KEY_ARG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshMaster.SshMasterFactory;

/**
 * Setups the SSH key and executes the command.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public abstract class AbstractSshRun extends AbstractCmdRun {

    @Inject
    protected TemplatesProvider templates;

    @Inject
    private SshMasterFactory sshMasterFactory;

    protected AbstractSshRun(Map<String, Object> args, Object parent, Threads threads) {
        super(args, parent, threads);
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        setupSshKey();
        setupSshMaster();
        setupRemote();
        try {
            String template = getCmdTemplate();
            TemplateResource res = templates.get().getResource(template);
            return runCommand(res, args);
        } finally {
            cleanupCmd();
        }
    }

    /**
     * Setups the remote before executing a command.
     *
     * @throws CommandExecException
     */
    protected void setupRemote() throws CommandExecException {
    }

    /**
     * Returns the command template that is executed by SSH.
     */
    protected abstract String getCmdTemplate();

    private void setupSshKey() throws SetupSshKeyException {
        URI key = (URI) args.get(SSH_KEY_ARG);
        if (key == null) {
            return;
        }
        File tmp = null;
        try {
            tmp = File.createTempFile("robobee", null);
            copySshKey(key, tmp);
            args.put(SSH_KEY_ARG, tmp);
        } catch (IOException e) {
            throw new SetupSshKeyException(e, key);
        }
    }

    private void copySshKey(URI key, File tmp) throws SetupSshKeyException {
        try {
            IOUtils.copy(key.toURL().openStream(), new FileOutputStream(tmp));
            tmp.setReadable(false, false);
            tmp.setReadable(true, true);
        } catch (IOException e) {
            tmp.delete();
            throw new SetupSshKeyException(e, key);
        }
    }

    private void setupSshMaster() throws CommandExecException {
        if (args.useSshMaster()) {
            sshMasterFactory.create(argsMap, parent, threads).call();
        }
    }

    private void cleanupCmd() {
        File sshKey = (File) args.get(SSH_KEY_ARG);
        if (sshKey != null) {
            sshKey.delete();
        }
    }

}
