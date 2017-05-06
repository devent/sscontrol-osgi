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
package com.anrisoftware.sscontrol.shell.internal.ssh;

import static com.anrisoftware.sscontrol.shell.external.Cmd.ENV_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SUDO_ENV_ARG;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.perf4j.slf4j.Slf4JStopWatch;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.shell.external.Cmd;
import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.shell.internal.st.StTemplate.StTemplateFactory;
import com.anrisoftware.sscontrol.shell.internal.templateres.TemplateResourceArgs.TemplateResourceArgsFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ShellImpl implements Shell {

    private static final String LOG_ARG = "log";

    private final Threads threads;

    private final Object parent;

    private final Map<String, Object> args;

    private final Object log;

    private final SshHost host;

    private final Map<String, String> env;

    private final Map<String, String> sudoEnv;

    @Inject
    private Cmd cmd;

    @Inject
    private TemplateResourceArgsFactory templateArgs;

    @Inject
    private StTemplateFactory stTemplateFactory;

    @Inject
    ShellImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<>(args);
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        this.host = host;
        this.env = new HashMap<>(getEnv("env", args));
        this.sudoEnv = new HashMap<>(getEnv("sudoEnv", args));
        setupArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        Slf4JStopWatch stopWatch = new Slf4JStopWatch("shell");
        try {
            String command = getCmd();
            return cmd.call(args, parent, threads, command);
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "ssh");
        } finally {
            stopWatch.stop();
        }
    }

    private String getCmd() {
        Object v = args.get(CMD_ARG);
        v = parseResourceArg(v);
        v = parseStArg(v);
        assertThat(format("args(%s)=null", CMD_ARG), v, notNullValue());
        return v.toString();
    }

    private Object parseStArg(Object v) {
        if (v != null) {
            return v;
        }
        return stTemplateFactory.create(args, parent).getText();
    }

    private Object parseResourceArg(Object v) {
        if (v != null) {
            return v;
        }
        Object base = args.get(BASE_ARG);
        Object res = args.get(RESOURCE_ARG);
        if (base != null || res != null) {
            v = templateArgs.create(args, parent).getText();
        }
        return v;
    }

    private void setupArgs() {
        args.put(LOG_ARG, log);
        args.put(ENV_ARG, env);
        args.put(SUDO_ENV_ARG, sudoEnv);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

    public Shell env(String string) {
        return putEnv(string, env);
    }

    public Shell env(Map<String, Object> args) {
        putEnvArgs(args, env);
        return this;
    }

    public Shell sudoEnv(String string) {
        return putEnv(string, sudoEnv);
    }

    public Shell sudoEnv(Map<String, Object> args) {
        putEnvArgs(args, sudoEnv);
        return this;
    }

    private Map<String, String> getEnv(String name, Map<String, Object> args) {
        @SuppressWarnings("unchecked")
        Map<String, String> env = (Map<String, String>) args.get(name);
        if (env == null) {
            env = new HashMap<>();
        }
        return env;
    }

    private Shell putEnv(String string, Map<String, String> env) {
        int i = string.indexOf('=');
        String key = string.substring(0, i);
        String value = string.substring(i + 1);
        env.put(key, value);
        return this;
    }

    private void putEnvArgs(Map<String, Object> args, Map<String, String> env) {
        Boolean literally = (Boolean) args.get("literally");
        String value = args.get("value").toString();
        String quote = "\'";
        if (literally != null && !literally) {
            quote = "\"";
        }
        value = String.format("%s%s%s", quote, value, quote);
        env.put(args.get("name").toString(), value);
    }

}
