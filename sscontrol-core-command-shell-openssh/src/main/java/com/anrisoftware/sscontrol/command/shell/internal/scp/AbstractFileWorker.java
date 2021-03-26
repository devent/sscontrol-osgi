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

import static com.anrisoftware.sscontrol.command.copy.external.Copy.DEST_ARG;
import static com.anrisoftware.sscontrol.command.fetch.external.Fetch.SRC_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.COMMAND_ARG;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AbstractFileWorker {

    @Inject
    private AbstractFileWorkerLogger log;

    @Inject
    private ScriptExecFactory scriptEx;

    @Inject
    @Assisted
    protected Map<String, Object> args;

    @Inject
    @Assisted
    private Object parent;

    @Inject
    @Assisted
    private Threads threads;

    @Inject
    @Assisted
    private Templates templates;

    @Inject
    protected LinuxPropertiesProvider linuxPropertiesProvider;

    public boolean isRemoteDest() {
        return (Boolean) args.get("remoteDest");
    }

    public boolean isRemoteSrc() {
        return (Boolean) args.get("remoteSrc");
    }

    public String getDest() {
        return args.get(DEST_ARG).toString();
    }

    public String getSrc() {
        return args.get(SRC_ARG).toString();
    }

    protected Boolean isRecursive() {
        Boolean recursive = (Boolean) args.get("recursive");
        return recursive != null && recursive;
    }

    protected Boolean isOverride() {
        Boolean recursive = (Boolean) args.get("override");
        return recursive != null && recursive;
    }

    protected ProcessTask runScript(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "scpCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    protected ProcessTask runCmd(Map<String, Object> args)
            throws CommandExecException {
        TemplateResource res = templates.getResource("ssh_wrap_bash");
        ProcessTask task;
        task = scriptEx.create(args, parent, threads, res, "sshCmd").call();
        log.commandFinished(parent, task, args);
        return task;
    }

    protected boolean isFileOnly() {
        Boolean fileOnly = (Boolean) args.get("fileOnly");
        return fileOnly != null ? fileOnly : false;
    }

    protected ProcessTask cleanFiles() throws CommandExecException {
        Map<String, Object> a = new HashMap<>(args);
        a.put("src", FilenameUtils.getName(getSrc()));
        a.put("remoteTmp", linuxPropertiesProvider.getRemoteTempDir());
        String cmd = linuxPropertiesProvider.getCleanFileCommands(a);
        a = new HashMap<>(args);
        a.put("privileged", false);
        a.put(COMMAND_ARG, cmd);
        return runCmd(a);
    }

}
