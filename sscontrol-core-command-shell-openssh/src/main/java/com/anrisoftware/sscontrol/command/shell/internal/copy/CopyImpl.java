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
package com.anrisoftware.sscontrol.command.shell.internal.copy;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.core.checkfilehash.HashName;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.copy.external.Copy;
import com.anrisoftware.sscontrol.command.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.copy.ScpCopyWorker.ScpCopyWorkerFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CopyImpl implements Copy {

    private static final Object FILES_LOCAL_ARG = "filesLocal";

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    @Inject
    private ScpCopyWorkerFactory scpFactory;

    @Inject
    private DownloadCopyWorkerFactory downloadFactory;

    @Inject
    CopyImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
    }

    @Override
    public ProcessTask call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("copy");
        try {
            parseHash();
            args.put("destOriginal", args.get("dest"));
            Boolean direct = (Boolean) args.get("direct");
            Copy copy = null;
            if (direct != null && direct) {
                copy = copyDirect();
            } else {
                copy = copyIndirect();
            }
            return copy.call();
        } finally {
            stopWatch.stop();
        }
    }

    private Copy copyIndirect() {
        Copy copy;
        copy = scpFactory.create(args, host, parent, threads, log);
        if (isFilesLocal()) {
            try {
                FileUtils.copyFile(getSrc(), getDest());
            } catch (IOException e) {
                throw new CopyFileException(e, getSrc(), getDest());
            }
        }
        return copy;
    }

    private Copy copyDirect() {
        Copy copy;
        copy = downloadFactory.create(args, host, parent, threads, log);
        return copy;
    }

    private File getDest() {
        return new File(args.get(DEST_ARG).toString());
    }

    private File getSrc() {
        return new File(args.get(SRC_ARG).toString());
    }

    private boolean isFilesLocal() {
        Boolean v = (Boolean) args.get(FILES_LOCAL_ARG);
        return v != null && v;
    }

    private void parseHash() {
        Object v = args.get(HASH_ARG);
        if (v == null) {
            return;
        }
        try {
            URI uri = new URI(v.toString());
            HashName hash = HashName.forResource(uri);
            args.put("hashType", hash.getFileExtention());
            args.put("hash", uri.getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            throw new InvalidHashSyntaxException(e, v);
        }
    }

}
