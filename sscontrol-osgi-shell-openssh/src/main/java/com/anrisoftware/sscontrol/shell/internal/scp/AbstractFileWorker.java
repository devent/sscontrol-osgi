/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.copy.external.Copy.DEST_ARG;
import static com.anrisoftware.sscontrol.fetch.external.Fetch.SRC_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.COMMAND_ARG;
import static java.lang.String.format;

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
 * @author Erwin Müller <erwin.mueller@deventm.de>
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
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String cmd = linuxPropertiesProvider.getCleanFileCommands();
        String src = FilenameUtils.getName(getSrc());
        Map<String, Object> a = new HashMap<>(args);
        a.put(COMMAND_ARG, format(cmd, tmp, src));
        return runCmd(a);
    }

}
