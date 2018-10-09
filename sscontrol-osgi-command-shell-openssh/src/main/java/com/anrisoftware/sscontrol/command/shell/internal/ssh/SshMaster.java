package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import java.util.Map;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.sscontrol.command.shell.external.ssh.AbstractCmdRun;
import com.anrisoftware.sscontrol.command.shell.external.ssh.TemplatesProvider;
import com.google.inject.assistedinject.Assisted;

/**
 * Executes the SSH shell to create the master.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshMaster extends AbstractCmdRun {

    public interface SshMasterFactory {

        SshMaster create(@Assisted Map<String, Object> args,
                @Assisted Object parent, @Assisted Threads threads);

    }

    @Inject
    private TemplatesProvider templates;

    @Inject
    SshMaster(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        super(args, parent, threads);
    }

    @Override
    public ProcessTask call() throws CommandExecException {
        createSocketDir(args);
        args.put("command", "ssh master");
        args.put("timeout", Duration.standardDays(1));
        Templates t = templates.get();
        TemplateResource sshmaster = t.getResource("ssh_master");
        return runCommand(sshmaster, args);
    }
}
