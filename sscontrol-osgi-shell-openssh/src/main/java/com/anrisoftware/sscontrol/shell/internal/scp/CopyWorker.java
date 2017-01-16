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

import static com.anrisoftware.sscontrol.shell.external.Cmd.COMMAND_ARG;
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
public class CopyWorker extends AbstractFileWorker
        implements Callable<ProcessTask> {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CopyWorkerFactory {

        CopyWorker create(Map<String, Object> args, Object parent,
                Threads threads, Templates templates,
                TemplateResource scriptRes);

    }

    @Inject
    @Assisted
    private TemplateResource scriptRes;

    @Override
    public ProcessTask call() throws CommandExecException {
        System.out.println(args.get("src")); // TODO println
        System.out.println(args.get("dest")); // TODO println
        System.out.println(args.get("override")); // TODO println
        ProcessTask task = null;
        if (!isOverride()) {
            CheckFiles check = new CheckFiles();
            task = check.call();
            if (check.fileExists) {
                return task;
            }
        }
        task = pushFiles();
        task = copyFiles();
        return task;
    }

    class CheckFiles implements Callable<ProcessTask> {

        boolean fileExists;

        @Override
        public ProcessTask call() throws CommandExecException {
            Map<String, Object> a = new HashMap<>(args);
            a.put("dest", getDest());
            String cmd = linuxPropertiesProvider.getCheckFileCommands(a);
            a = new HashMap<>(args);
            a.put(COMMAND_ARG, cmd);
            a.put("exitCodes", new int[] { 0, 1 });
            ProcessTask task = runCmd(a);
            switch (task.getExitValue()) {
            case 0:
                this.fileExists = false;
                break;
            case 1:
                this.fileExists = true;
                break;
            }
            return task;
        }

    }

    private ProcessTask pushFiles() throws CommandExecException {
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String src = FilenameUtils.getName(getSrc());
        Map<String, Object> a = new HashMap<>(args);
        a.put("dest", format("%s/%s", tmp, src));
        a.put("destOriginal", args.get("dest"));
        return runScript(scriptRes, a);
    }

    protected ProcessTask copyFiles() throws CommandExecException {
        Map<String, Object> a = new HashMap<>(args);
        a.put("src", FilenameUtils.getName(getSrc()));
        a.put("remoteTmp", linuxPropertiesProvider.getRemoteTempDir());
        String cmd = linuxPropertiesProvider.getPushFileCommands(a);
        a = new HashMap<>(args);
        a.put(COMMAND_ARG, cmd);
        return runCmd(a);
    }

}
