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
package com.anrisoftware.sscontrol.command.shell.linux.openssh.internal.find

import javax.inject.Inject

import org.perf4j.slf4j.Slf4JStopWatch

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.command.shell.external.Shell
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFiles
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
