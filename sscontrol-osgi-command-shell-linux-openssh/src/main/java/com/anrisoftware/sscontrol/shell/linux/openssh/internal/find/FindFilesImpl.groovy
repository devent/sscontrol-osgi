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
package com.anrisoftware.sscontrol.shell.linux.openssh.internal.find

import javax.inject.Inject

import org.perf4j.slf4j.Slf4JStopWatch

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.shell.external.Shell
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.shell.linux.openssh.external.find.FindFiles
import com.anrisoftware.sscontrol.types.app.external.AppException
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.google.inject.assistedinject.Assisted

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FindFilesImpl implements FindFiles {

    private final Map<String, Object> args

    private final SshHost host

    private final Object parent

    private final Threads threads

    private final Object log

    private final Collection patterns

    @Inject
    private ShellFactory shellFactory

    @Inject
    FindFilesImpl(TemplatesFactory templatesFactory,
    @Assisted Map<String, Object> args, @Assisted SshHost host,
    @Assisted("parent") Object parent, @Assisted Threads threads,
    @Assisted("log") Object log) {
        this.args = new HashMap<>(args)
        this.host = host
        this.parent = parent
        this.threads = threads
        this.log = log
        this.patterns = []
        def c = parseSuffix(args)
        if (c) {
            this.patterns.addAll c
        }
        c = parsePatterns(args)
        if (c) {
            this.patterns.addAll parsePatterns(args)
        }
    }

    private Collection parseSuffix(Map<String, Object> args) {
        Object v = args.get(SUFFIX_ARG)
        if (v != null) {
            Collection c = v
            return c.inject([]){ list, value ->
                list << "\\*.$value"
            }
        }
        return null
    }

    private Collection parsePatterns(Map<String, Object> args) {
        Object v = args.get(PATTERNS_ARG)
        if (v != null) {
            return v
        }
        return null
    }

    @Override
    public List<String> call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("findFiles")
        try {
            return runCmd()
        } finally {
            stopWatch.stop()
        }
    }

    private List<String> runCmd() {
        def a = new HashMap(args)
        a.outString = true
        a.vars = [patterns: patterns]
        a.st = "find . -type f <\\u005C>( <vars.patterns:{p|-name <p>};separator=\" -or \"> <\\u005C>)"
        Shell shell = shellFactory.create(a, host, parent, threads, log)
        def process = shell.call()
        def files = process.out.split(/\n/)
        return files.sort(false)
    }
}
