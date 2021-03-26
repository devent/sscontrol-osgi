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
package com.anrisoftware.sscontrol.command.shell.internal.fetch;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class FetchImpl implements Fetch {

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    @Inject
    private ScpFactory scpFactory;

    @Inject
    FetchImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        checkArgs();
        setupArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("fetch");
        try {
            return scpFactory.create(args, host, parent, threads, log).call();
        } finally {
            stopWatch.stop();
        }
    }

    private void checkArgs() {
        isTrue(args.containsKey(SRC_ARG), "%s=null", SRC_ARG);
        notNull(args.get(SRC_ARG), "%s=null", SRC_ARG);
    }

    private void setupArgs() {
        args.put("remoteSrc", true);
        args.put("remoteDest", false);
        String src = args.get(SRC_ARG).toString();
        args.put(LOG_ARG, log);
        args.put(SRC_ARG, new File(src));
        if (args.get(DEST_ARG) == null) {
            isTrue(args.containsKey(PWD_ARG), "%s=null", PWD_ARG);
            notNull(args.get(PWD_ARG), "%s=null", PWD_ARG);
            File pwd = (File) args.get(PWD_ARG);
            args.put(DEST_ARG, pwd);
        } else {
            String dest = args.get(DEST_ARG).toString();
            args.put(DEST_ARG, new File(dest));
        }
    }

}
