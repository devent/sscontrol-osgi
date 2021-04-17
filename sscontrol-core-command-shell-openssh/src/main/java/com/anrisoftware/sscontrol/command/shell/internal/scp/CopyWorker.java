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

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.COMMAND_ARG;
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
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CopyWorker extends AbstractFileWorker
        implements Callable<ProcessTask> {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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

    /**
     * Checks if the file exists on the remote.
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
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
