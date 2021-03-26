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
package com.anrisoftware.sscontrol.command.shell.internal.template;

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_EXTERNAL_CONTROL_PATH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_USER_ARG;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.command.shell.external.template.WriteTemplateException;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResourceArgs.TemplateResourceArgsFactory;
import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("fetch");
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
            stopWatch.stop();
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
        args.put(SSH_EXTERNAL_CONTROL_PATH_ARG, host.getSocket());
    }

    private void parseArgs() {
        Object v = args.get("tmpFile");
        if (v != null) {
            this.tmpFile = (File) v;
        }
    }

}
