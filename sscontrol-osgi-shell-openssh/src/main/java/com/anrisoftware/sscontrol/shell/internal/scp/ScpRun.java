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

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.sscontrol.shell.external.ssh.AbstractSshRun;
import com.anrisoftware.sscontrol.shell.internal.scp.CopyWorker.CopyWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.scp.FetchWorker.FetchWorkerFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Executes the scp command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ScpRun extends AbstractSshRun {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ScpRunFactory {

        ScpRun create(@Assisted Map<String, Object> args,
                @Assisted Object parent, @Assisted Threads threads);

    }

    @Inject
    private ScpRunLogger log;

    @Inject
    private FetchWorkerFactory fetch;

    @Inject
    private CopyWorkerFactory push;

    @Inject
    private LinuxPropertiesProvider linuxPropertiesProvider;

    private Map<String, Object> scpArgs;

    @Inject
    ScpRun(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        super(args, parent, threads);
    }

    @Override
    protected void setupRemote() throws CommandExecException {
        ProcessTask task;
        String template = "ssh_wrap_bash";
        TemplateResource res = templates.get().getResource(template);
        Map<String, Object> a = new HashMap<>(args);
        a.put("privileged", true);
        a.put(COMMAND_ARG, format(linuxPropertiesProvider.getSetupCommands(),
                linuxPropertiesProvider.getRemoteTempDir()));
        task = scriptEx.create(a, parent, threads, res, "sshCmd").call();
        log.setupRemoteFinished(parent, task, a);
    }

    @Override
    protected String getCmdTemplate() {
        return "scp";
    }

    @Override
    protected ProcessTask runCommand(TemplateResource res,
            Map<String, Object> args) throws CommandExecException {
        this.scpArgs = new HashMap<>(this.args);
        setupDefaults(scpArgs);
        ProcessTask task = null;
        task = run(res);
        return task;
    }

    private void setupDefaults(Map<String, Object> args) {
        Object v = args.get("override");
        if (v == null) {
            args.put("override", true);
        }
    }

    private ProcessTask run(TemplateResource res) throws CommandExecException {
        ProcessTask task = null;
        if (isRemoteSrc(scpArgs)) {
            return fetch.create(scpArgs, parent, threads, templates.get(), res)
                    .call();
        }
        if (isRemoteDest(scpArgs)) {
            return push.create(scpArgs, parent, threads, templates.get(), res)
                    .call();
        }
        return task;
    }

    private boolean isRemoteDest(Map<String, Object> args) {
        return (Boolean) args.get("remoteDest");
    }

    private boolean isRemoteSrc(Map<String, Object> args) {
        return (Boolean) args.get("remoteSrc");
    }

}
