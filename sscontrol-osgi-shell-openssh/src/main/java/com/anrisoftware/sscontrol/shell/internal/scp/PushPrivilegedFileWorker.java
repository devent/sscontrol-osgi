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
import static com.anrisoftware.sscontrol.shell.external.Cmd.COMMAND_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.PRIVILEGED_ARG;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
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
public class PushPrivilegedFileWorker extends AbstractFileWorker
        implements Callable<ProcessTask> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface PushPrivilegedFileWorkerFactory {

        PushPrivilegedFileWorker create(Map<String, Object> args, Object parent,
                Threads threads, Templates templates,
                TemplateResource scriptRes);

    }

    @Inject
    @Assisted
    private TemplateResource scriptRes;

    @Inject
    private LinuxPropertiesProvider linuxPropertiesProvider;

    @Override
    public ProcessTask call() throws CommandExecException {
        ProcessTask task = null;
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String cmd = linuxPropertiesProvider.getPushFileCommands();
        String src = FilenameUtils.getName(getSrc());
        String dest = getDest();
        args.put(DEST_ARG, tmp);
        task = runScript(scriptRes, args);
        Map<String, Object> a = new HashMap<String, Object>(args);
        a.put(PRIVILEGED_ARG, true);
        a.put(COMMAND_ARG, format(cmd, tmp, src, dest));
        task = runCmd(a);
        return task;
    }

}