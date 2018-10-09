package com.anrisoftware.sscontrol.command.replace.external;

import java.util.Map;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Replace command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Replace {

    static final String CHARSET_ARG = "charset";

    static final String REPLACE_ARG = "replace";

    static final String SEARCH_ARG = "search";

    static final String END_TOKEN_ARG = "endToken";

    static final String BEGIN_TOKEN_ARG = "beginToken";

    static final String DEST_ARG = "dest";

    /**
     * Temporary file name.
     */
    static final String TMP_ARG = "tmp";

    /**
     * Factory to create the replace command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ReplaceFactory {

        Replace create(@Assisted Map<String, Object> args,
                @Assisted SshHost host, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);
    }

    /**
     * Executes the replacement.
     * 
     * @throws AppException
     */
    Replace call() throws AppException;

}
