package com.anrisoftware.sscontrol.command.fetch.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Fetch command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Fetch {

    static final String PWD_ARG = "pwd";

    static final String SRC_ARG = "src";

    static final String DEST_ARG = "dest";

    static final String LOG_ARG = "log";

    /**
     * Factory to create the fetch command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FetchFactory {

        Fetch create(@Assisted Map<String, Object> args, @Assisted SshHost host,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log);
    }

    /**
     * Executes the fetch command.
     * 
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
