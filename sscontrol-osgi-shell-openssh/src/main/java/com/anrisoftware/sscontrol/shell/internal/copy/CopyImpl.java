/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.shell.internal.copy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.checkfilehash.HashName;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.copy.external.Copy;
import com.anrisoftware.sscontrol.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.copy.ScpCopyWorker.ScpCopyWorkerFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CopyImpl implements Copy {

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
        parseHash();
        args.put("destOriginal", args.get("dest"));
        Boolean direct = (Boolean) args.get("direct");
        Copy copy = null;
        if (direct != null && direct) {
            copy = downloadFactory.create(args, host, parent, threads, log);
        } else {
            copy = scpFactory.create(args, host, parent, threads, log);
        }
        return copy.call();
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
