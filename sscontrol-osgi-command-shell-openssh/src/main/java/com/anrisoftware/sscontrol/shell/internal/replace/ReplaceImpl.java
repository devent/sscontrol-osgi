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
package com.anrisoftware.sscontrol.shell.internal.replace;

import static org.apache.commons.lang3.Validate.isTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.shell.internal.replace.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceLine.ReplaceLineFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceWorker.ReplaceWorkerFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
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
        this.args = new HashMap<String, Object>(args);
        this.lines = new ArrayList<ReplaceLine>();
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.cmdLog = cmdLog;
        setupDefaults(propertiesProvider);
        checkArgs();
    }

    public void line(String replace) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("replace", replace);
        line(args);
    }

    public void line(Map<String, Object> args) {
        ReplaceLine line = lineFactory.create(args);
        log.lineAdded(this, line);
        lines.add(line);
    }

    @Override
    public Replace call() throws AppException {
        String text = load.create(args, host, parent, threads, cmdLog).call();
        if (lines.size() == 0) {
            lines.add(lineFactory.create(args));
        }
        text = setupReplaceLines(text);
        push.create(args, host, parent, threads, cmdLog, text).call();
        return this;
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
