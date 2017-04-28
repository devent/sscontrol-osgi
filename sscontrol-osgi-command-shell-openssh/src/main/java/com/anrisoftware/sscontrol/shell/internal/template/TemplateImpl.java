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
package com.anrisoftware.sscontrol.shell.internal.template;

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.shell.external.template.WriteTemplateException;
import com.anrisoftware.sscontrol.shell.internal.scp.ScpRun.ScpRunFactory;
import com.anrisoftware.sscontrol.shell.internal.templateres.TemplateResourceArgs.TemplateResourceArgsFactory;
import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TemplateImpl implements Template {

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final String text;

    private File tmpFile;

    @Inject
    private ScpRunFactory scpRunFactory;

    @Inject
    TemplateImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log,
            TemplateResourceArgsFactory templateFactory) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.text = templateFactory.create(args, parent).getText();
        setupArgs();
        parseArgs();
        checkArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        File file = null;
        try {
            file = getTmpFile();
            FileUtils.write(file, text, getCharset());
            Map<String, Object> a = new HashMap<>(args);
            a.put("src", file);
            a.put("log", log);
            return scpRunFactory.create(a, parent, threads).call();
        } catch (IOException e) {
            throw new WriteTemplateException(e, args);
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "scp");
        } finally {
            if (tmpFile == null) {
                if (file != null) {
                    file.delete();
                }
            }
        }
    }

    private File getTmpFile() throws IOException {
        File file = tmpFile;
        if (file == null) {
            file = File.createTempFile("template", null);
        }
        return file;
    }

    private Charset getCharset() {
        return (Charset) args.get(CHARSET_ARG);
    }

    private void checkArgs() {
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupArgs() {
        args.put("remoteSrc", false);
        args.put("remoteDest", true);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

    private void parseArgs() {
        Object v = args.get("tmpFile");
        if (v != null) {
            this.tmpFile = (File) v;
        }
    }

}
