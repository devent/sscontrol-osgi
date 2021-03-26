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
package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static org.apache.commons.lang3.Validate.isTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.command.replace.external.Replace;
import com.anrisoftware.sscontrol.command.shell.internal.replace.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceLine.ReplaceLineFactory;
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceWorker.ReplaceWorkerFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ReplaceImpl implements Replace {

    private final Map<String, Object> args;

    private final List<ReplaceLine> lines;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object cmdLog;

    @Inject
    private ReplaceImplLogger log;

    @Inject
    private ReplaceWorkerFactory replace;

    @Inject
    private LoadFileWorkerFactory load;

    @Inject
    private PushFileWorkerFactory push;

    @Inject
    private ReplaceLineFactory lineFactory;

    @Inject
    ReplaceImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object cmdLog,
            PropertiesProvider propertiesProvider) {
        this.args = new HashMap<>(args);
        this.lines = new ArrayList<>();
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.cmdLog = cmdLog;
        setupDefaults(propertiesProvider);
        checkArgs();
    }

    public void line(String replace) {
        Map<String, Object> args = new HashMap<>();
        args.put("replace", replace);
        line(args);
    }

    /**
     * <pre>
     * replace with {
     *     line search: "s/search/replace/" // or
     *     line search: "search", replace: "replace"
     * }
     * </pre>
     */
    public void line(Map<String, Object> args) {
        ReplaceLine line = lineFactory.create(args);
        log.lineAdded(this, line);
        lines.add(line);
    }

    @Override
    public Replace call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("replace");
        try {
            doReplace();
            return this;
        } finally {
            stopWatch.stop();
        }
    }

    private void doReplace() {
        String text = load.create(args, host, parent, threads, cmdLog).call();
        if (lines.size() == 0) {
            lines.add(lineFactory.create(args));
        }
        text = setupReplaceLines(text);
        push.create(args, host, parent, threads, cmdLog, text).call();
    }

    private String setupReplaceLines(String text) throws AppException {
        for (ReplaceLine line : lines) {
            Map<String, Object> a = line.getArgs(args);
            TokensTemplate tokens = replace.create(a, text).call();
            text = tokens.getText();
        }
        return text;
    }

    private void checkArgs() {
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupDefaults(PropertiesProvider propertiesProvider) {
        ContextProperties p = propertiesProvider.getProperties();
        Object arg = args.get(CHARSET_ARG);
        if (arg == null) {
            args.put(CHARSET_ARG, p.getCharsetProperty("default_charset"));
        }
    }

}
