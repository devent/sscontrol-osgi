package com.anrisoftware.sscontrol.template.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Template command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Template {

    static final String BASE_ARG = "base";

    static final String ATTRIBUTES_ARG = "attributes";

    static final String CLASS_LOADER_ARG = "classLoader";

    static final String CONTROL_ARG = "control";

    static final String RESOURCE_ARG = "resource";

    static final String LOCALE_ARG = "locale";

    static final String VARS_ARG = "vars";

    static final String NAME_ARG = "name";

    static final String CHARSET_ARG = "charset";

    static final String SRC_ARG = "src";

    static final String DEST_ARG = "dest";

    /**
     * Factory to create the template command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TemplateFactory {

        Template create(@Assisted Map<String, Object> args,
                @Assisted SshHost target, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);
    }

    /**
     * Executes the template command.
     * 
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
