package com.anrisoftware.sscontrol.command.shell.external.ssh;

import java.util.Map;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface CmdArgs {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface CmdArgsFactory {

        CmdArgs create(Map<String, Object> args);

    }

    SshArgs getArgs();

}
