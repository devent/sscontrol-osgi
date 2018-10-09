package com.anrisoftware.sscontrol.command.shell.internal.scp;

import static com.anrisoftware.sscontrol.command.fetch.external.Fetch.SRC_ARG;
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
 * Fetches a file from the remote destination.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FetchWorker extends AbstractFileWorker
        implements Callable<ProcessTask> {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FetchWorkerFactory {

        FetchWorker create(Map<String, Object> args, Object parent,
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
        task = copyFiles();
        task = fetchFiles();
        task = cleanFiles();
        return task;
    }

    protected ProcessTask copyFiles() throws CommandExecException {
        Map<String, Object> a = new HashMap<>(args);
        a.put("src", getSrc());
        a.put("remoteTmp", linuxPropertiesProvider.getRemoteTempDir());
        String cmd = linuxPropertiesProvider.getCopyFileCommands(a);
        a = new HashMap<>(args);
        a.put(COMMAND_ARG, cmd);
        return runCmd(a);
    }

    private ProcessTask fetchFiles() throws CommandExecException {
        String tmp = linuxPropertiesProvider.getRemoteTempDir();
        String src = FilenameUtils.getName(getSrc());
        Map<String, Object> a = new HashMap<>(args);
        a.put(SRC_ARG, format("%s/%s", tmp, src));
        return runScript(scriptRes, a);
    }
}
