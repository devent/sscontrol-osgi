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

import static com.anrisoftware.sscontrol.replace.external.Replace.CHARSET_ARG;
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
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory;
import com.anrisoftware.sscontrol.replace.external.ReadFileException;
import com.anrisoftware.sscontrol.shell.internal.replace.CreateTempFileWorker.CreateTempFileWorkerFactory;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class PushFileWorker implements Callable<Void> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface PushFileWorkerFactory {

        PushFileWorker create(@Assisted Map<String, Object> args,
                @Assisted SshHost host, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log,
                @Assisted String text);

    }

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final String text;

    private final Charset encoding;

    @Inject
    private CopyFactory copyFactory;

    @Inject
    private CreateTempFileWorkerFactory temp;

    @Inject
    PushFileWorker(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log, @Assisted String text) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.text = text;
        this.encoding = (Charset) args.get(CHARSET_ARG);
        checkArgs();
    }

    @Override
    public Void call() throws AppException {
        File tmp = temp.create(args).call();
        try {
            args.put("src", tmp);
            FileUtils.write(tmp, text, encoding);
            copyFactory.create(args, host, parent, threads, log).call();
            return null;
        } catch (IOException e) {
            throw new ReadFileException(tmp, e);
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
        }
    }

    private void checkArgs() {
        notNull(args.get(CHARSET_ARG), "%s=null", CHARSET_ARG);
    }

}