package com.anrisoftware.sscontrol.command.copy.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Copy command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Copy {

    static final String PWD_ARG = "pwd";

    static final String SRC_ARG = "src";

    static final String DEST_ARG = "dest";

    static final String LOG_ARG = "log";

    static final String HASH_ARG = "hash";

    static final String SIG_ARG = "sig";

    static final String SERVER_ARG = "server";

    static final String KEY_ARG = "key";

    static final String DIRECT_ARG = "direct";

    static final String PRIVILEGED_ARG = "privileged";

    static final String SIG_REMOTE_ARGS = "sigRemote";

    static final String REMOTE_TMP_ARGS = "remoteTmp";

    /**
     * Factory to create the copy command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CopyFactory {

        Copy create(@Assisted Map<String, Object> args, @Assisted SshHost ssh,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log);
    }

    /**
     * Executes the copy command.
     *
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
