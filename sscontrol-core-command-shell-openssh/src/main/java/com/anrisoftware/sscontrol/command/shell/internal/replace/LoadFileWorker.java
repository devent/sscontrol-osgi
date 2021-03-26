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

import static com.anrisoftware.sscontrol.command.replace.external.Replace.CHARSET_ARG;
import static com.anrisoftware.sscontrol.command.replace.external.Replace.DEST_ARG;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory;
import com.anrisoftware.sscontrol.command.replace.external.ReadFileException;
import com.anrisoftware.sscontrol.command.shell.internal.replace.CreateTempFileWorker.CreateTempFileWorkerFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class LoadFileWorker implements Callable<String> {

    private static final String SRC_ARG = "src";

    /**
     * 
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface LoadFileWorkerFactory {

        LoadFileWorker create(@Assisted Map<String, Object> args,
                @Assisted SshHost host, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);

    }

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final Charset encoding;

    private final Object dest;

    @Inject
    private FetchFactory fetchFactory;

    @Inject
    private CreateTempFileWorkerFactory temp;

    @Inject
    LoadFileWorker(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        checkArgs();
        this.dest = args.get(DEST_ARG);
        this.encoding = (Charset) args.get(CHARSET_ARG);
    }

    @Override
    public String call() throws AppException {
        File tmp = temp.create(args).call();
        try {
            args.put(SRC_ARG, dest);
            args.put(DEST_ARG, tmp);
            fetchFactory.create(args, host, parent, threads, log).call();
            return FileUtils.readFileToString(tmp, encoding);
        } catch (IOException e) {
            throw new ReadFileException(tmp, e);
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
        }
    }

    private void checkArgs() {
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(CHARSET_ARG), "%s=null", CHARSET_ARG);
    }

}
