/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.command.shell.internal.facts;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.sscontrol.command.facts.external.Facts;
import com.anrisoftware.sscontrol.command.shell.external.Shell;
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.command.shell.internal.facts.CatReleaseParse.CatReleaseParseFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
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

    private SystemInfo system;

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
    public SystemInfo getSystem() {
        return system;
    }

    @Override
    public ProcessTask getProcess() {
        return process;
    }

    @Override
    public Facts call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("facts");
        try {
            runFacts();
            return this;
        } finally {
            stopWatch.stop();
        }
    }

    private void runFacts() {
        TemplateResource res = templates.getResource("facts");
        Map<String, Object> a = new HashMap<>(args);
        a.put("outString", true);
        a.put("resource", res);
        a.put("name", "factsCmd");
        Shell shell = shellFactory.create(a, host, parent, threads, log);
        this.process = shell.call();
        this.system = catReleaseParse.create(process.getOut()).call();
    }

    private void checkArgs() {
    }

    private void setupArgs() {
        args.put("remoteSrc", false);
        args.put("remoteDest", true);
        args.put(LOG_ARG, log);
    }

}
