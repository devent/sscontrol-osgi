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
package com.anrisoftware.sscontrol.command.shell.internal.cmd;

import static com.anrisoftware.sscontrol.command.shell.external.Cmd.DEBUG_LEVEL_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.ENV_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SHELL_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_CONNECTION_TIMEOUT_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_CONTROL_MASTER_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_CONTROL_PATH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_CONTROL_PERSIST_DURATION_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_EXTERNAL_CONTROL_PATH_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SSH_USER_ARG;
import static com.anrisoftware.sscontrol.command.shell.external.Cmd.SUDO_ENV_ARG;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.durationformat.DurationFormatFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.ParsePropertiesErrorException;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs.SshArgsFactory;
import com.anrisoftware.sscontrol.command.shell.internal.cmd.SshOptions.SshOptionsFactory;
import com.anrisoftware.sscontrol.command.shell.internal.ssh.PropertiesProvider;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
class CmdArgsImpl implements CmdArgs {

    private final SshArgs args;

    @Inject
    private PropertiesProvider propertiesProvider;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    @Inject
    private SshOptionsFactory sshOptionsFactory;

    @Inject
    CmdArgsImpl(@Assisted Map<String, Object> args,
            SshArgsFactory argsMapFactory) {
        this.args = argsMapFactory.create(new HashMap<String, Object>(args));
    }

    @Override
    public SshArgs getArgs() {
        setupDefaults();
        setupSshDefaultArgs();
        return args;
    }

    private void setupDefaults() {
        Object arg = args.get(SHELL_ARG);
        ContextProperties p = propertiesProvider.get().withSystemReplacements();
        if (arg == null) {
            String shell = p.getProperty("default_shell");
            args.put(SHELL_ARG, shell);
        }
        arg = args.get(SSH_USER_ARG);
        if (arg == null) {
            args.put(SSH_USER_ARG, p.getProperty("default_ssh_user"));
        }
        arg = args.get(SSH_PORT_ARG);
        if (arg == null) {
            args.put(SSH_PORT_ARG,
                    p.getNumberProperty("default_ssh_port").intValue());
        }
        arg = args.get(SSH_ARG);
        if (arg == null) {
            args.put(SSH_ARG, p.getListProperty("default_ssh_args", ";"));
        }
        arg = args.get(SSH_CONTROL_MASTER_ARG);
        if (arg == null) {
            args.put(SSH_CONTROL_MASTER_ARG,
                    p.getProperty("default_ssh_control_master"));
        }
        arg = args.get(SSH_CONTROL_PERSIST_DURATION_ARG);
        if (arg == null) {
            args.put(SSH_CONTROL_PERSIST_DURATION_ARG, getDefaultDuration(p,
                    "default_ssh_control_persist_duration"));
        }
        arg = args.get(SSH_CONTROL_PATH_ARG);
        if (arg == null) {
            args.put(SSH_CONTROL_PATH_ARG,
                    p.getProperty("default_ssh_control_path"));
        }
        arg = args.get(SSH_CONNECTION_TIMEOUT_ARG);
        if (arg == null) {
            args.put(SSH_CONNECTION_TIMEOUT_ARG,
                    getDefaultDuration(p, "default_ssh_connect_timeout"));
        }
        arg = args.get(DEBUG_LEVEL_ARG);
        if (arg == null) {
            args.put(DEBUG_LEVEL_ARG,
                    p.getNumberProperty("default_ssh_debug_level").intValue());
        }
        arg = args.get(ENV_ARG);
        if (arg == null) {
            args.put(ENV_ARG, new HashMap<String, String>());
        }
        arg = args.get(SUDO_ENV_ARG);
        if (arg == null) {
            args.put(SUDO_ENV_ARG, new HashMap<String, String>());
        }
    }

    private void setupSshDefaultArgs() {
        List<String> options = new ArrayList<String>();
        args.put("sshDefaultOptions", options);
        SshOptions sshOptions = sshOptionsFactory.create(args, options);
        sshOptions.addOption(SSH_EXTERNAL_CONTROL_PATH_ARG,
                "ssh_external_control_path_option");
        sshOptions.addDefaultOptions();
        sshOptions.addDebug();
        if (args.useSshMaster()) {
            sshOptions.addStringOption(SSH_CONTROL_MASTER_ARG,
                    "ssh_control_master_option");
            sshOptions.addOption(SSH_CONTROL_PERSIST_DURATION_ARG,
                    "ssh_control_persist_option");
            sshOptions.addOption(SSH_CONNECTION_TIMEOUT_ARG,
                    "ssh_connection_timeout_option");
            sshOptions.addControlPathOption(SSH_CONTROL_PATH_ARG,
                    "ssh_control_path_option");
        }
    }

    private Object getDefaultDuration(ContextProperties p, String property) {
        try {
            return p.getTypedProperty(property, durationFormatFactory.create());
        } catch (ParseException e) {
            throw new ParsePropertiesErrorException(e, property);
        }
    }

}
