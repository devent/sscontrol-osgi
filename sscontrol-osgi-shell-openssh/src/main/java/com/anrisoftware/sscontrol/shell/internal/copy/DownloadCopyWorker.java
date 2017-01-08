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
package com.anrisoftware.sscontrol.shell.internal.copy;

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.sscontrol.copy.external.Copy;
import com.anrisoftware.sscontrol.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.shell.internal.ssh.SshRun.SshRunFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DownloadCopyWorker implements Copy {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DownloadCopyWorkerFactory {

        DownloadCopyWorker create(@Assisted Map<String, Object> args,
                @Assisted SshHost host, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);

    }

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final TemplateResource cmdTemplate;

    @Inject
    private SshRunFactory runFactory;

    @Inject
    private LinuxPropertiesProvider linuxPropertiesProvider;

    @Inject
    DownloadCopyWorker(TemplatesFactory templates,
            @Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.cmdTemplate = templates.create("CopyTemplates")
                .getResource("download_cmd");
        setupArgs();
        checkArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        try {
            cmdTemplate.invalidate();
            Map<String, Object> a = new HashMap<>(args);
            a.put("tmp", linuxPropertiesProvider.getRemoteTempDir());
            a.put("withSudo", a.get("privileged"));
            a.put("privileged", null);
            String command = cmdTemplate.getText("downloadCmd", "args", a);
            return runFactory.create(a, parent, threads, command).call();
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "downloadCmd");
        }
    }

    private void checkArgs() {
        isTrue(args.containsKey(SRC_ARG), "%s=null", SRC_ARG);
        notNull(args.get(SRC_ARG), "%s=null", SRC_ARG);
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupArgs() {
        args.put(LOG_ARG, log);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

}
