/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.shell.internal.facts;

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.sscontrol.facts.external.Facts;
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.shell.internal.facts.CatReleaseParse.CatReleaseParseFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.HostSystem;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FactsImpl implements Facts {

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final Templates templates;

    @Inject
    private ShellFactory shellFactory;

    @Inject
    private CatReleaseParseFactory catReleaseParse;

    private HostSystem system;

    private ProcessTask process;

    @Inject
    FactsImpl(TemplatesFactory templatesFactory,
            @Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.templates = templatesFactory.create("FactsTemplates");
        setupArgs();
        checkArgs();
    }

    @Override
    public HostSystem getSystem() {
        return system;
    }

    @Override
    public ProcessTask getProcess() {
        return process;
    }

    @Override
    public Facts call() throws AppException {
        String cmd = templates.getResource("facts").getText("factsCmd", "args",
                args);
        Map<String, Object> a = new HashMap<>(args);
        a.put("outString", true);
        this.process = shellFactory.create(a, host, parent, threads, log, cmd)
                .call();
        this.system = catReleaseParse.create(process.getOut()).call();
        return this;
    }

    private void checkArgs() {
    }

    private void setupArgs() {
        args.put("remoteSrc", false);
        args.put("remoteDest", true);
        args.put(LOG_ARG, log);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

}
