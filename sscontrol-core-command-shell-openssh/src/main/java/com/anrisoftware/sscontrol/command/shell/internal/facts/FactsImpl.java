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
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
