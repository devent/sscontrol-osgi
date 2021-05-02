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

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_EXTERNAL_CONTROL_PATH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_USER_ARG;
import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.sscontrol.command.copy.external.Copy;
import com.anrisoftware.sscontrol.command.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshRun.SshRunFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class DownloadCopyWorker implements Copy {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface DownloadCopyWorkerFactory {

        DownloadCopyWorker create(@Assisted Map<String, Object> args, @Assisted SshHost host,
                @Assisted("parent") Object parent, @Assisted Threads threads, @Assisted("log") Object log);

    }

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private final TemplateResource cmdTemplate;

    @Inject
    private SshRunFactory runFactory;

    @Inject
    private LinuxPropertiesProvider linuxPropertiesProvider;

    @Inject
    DownloadCopyWorker(TemplatesFactory templates, @Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads, @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.cmdTemplate = templates.create("CopyTemplates").getResource("download_cmd");
        setupArgs();
        checkArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        try {
            cmdTemplate.invalidate();
            Map<String, Object> a = new HashMap<>(args);
            a.put("remoteTmp", linuxPropertiesProvider.getRemoteTempDir(args));
            a.put("withSudo", a.get("privileged"));
            a.put("privileged", null);
            String command = cmdTemplate.getText("downloadCmd", "args", a);
            return runFactory.create(a, parent, threads, command).call();
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "downloadCmd");
        }
    }

    private void checkArgs() {
        isTrue(args.containsKey(SRC_ARG), "%s=null", SRC_ARG);
        notNull(args.get(SRC_ARG), "%s=null", SRC_ARG);
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
        Object v = args.get(SIG_ARG);
        if (v != null) {
            assertThat(format("%s=null", SERVER_ARG), args.get(SERVER_ARG), is(notNullValue()));
            assertThat(format("%s=null", KEY_ARG), args.get(KEY_ARG), is(notNullValue()));
        }
    }

    private void setupArgs() {
        args.put(LOG_ARG, log);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
        args.put(SSH_EXTERNAL_CONTROL_PATH_ARG, host.getSocket());
        Object v = args.get(SIG_ARG);
        if (v != null) {
            sigRemote(args, v.toString());
        }
    }

    private void sigRemote(Map<String, Object> args, String v) {
        try {
            sigRemote0(v);
        } catch (URISyntaxException e) {
        }
    }

    private void sigRemote0(String v) throws URISyntaxException {
        URI sig = new URI(v);
        boolean sigRemote = false;
        switch (sig.getScheme()) {
        case "http":
            sigRemote = true;
            break;
        case "https":
            sigRemote = true;
            break;
        case "ftp":
            sigRemote = true;
            break;
        default:
            break;
        }
        args.put(SIG_REMOTE_ARGS, sigRemote);
    }

}
