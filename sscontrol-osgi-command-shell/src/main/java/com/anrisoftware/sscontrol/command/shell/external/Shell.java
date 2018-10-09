package com.anrisoftware.sscontrol.command.shell.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Shell command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Shell {

    static final String CMD_ARG = "command";

    static final String BASE_ARG = "base";

    static final String ATTRIBUTES_ARG = "attributes";

    static final String CLASS_LOADER_ARG = "classLoader";

    static final String CONTROL_ARG = "control";

    static final String RESOURCE_ARG = "resource";

    static final String LOCALE_ARG = "locale";

    static final String VARS_ARG = "vars";

    /**
     * The string passed is used a StringTemplate template. The {@code parent}
     * and {@code vars} arguments are passed to the template. For example,
     *
     * <pre>
     * shell st: "<vars.patterns:{p|find <p>};separator="\n">",
     * vars: [patterns: ["*.yaml", "*.json"]] call()
     * </pre>
     */
    static final String ST_ARG = "st";

    /**
     * Factory to create the shell command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ShellFactory {

        Shell create(@Assisted Map<String, Object> args, @Assisted SshHost host,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log);
    }

    /**
     * Executes the shell.
     *
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
