package com.anrisoftware.sscontrol.command.shell.external.ssh;

import java.io.IOException;
import java.net.URI;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SetupSshKeyException extends CommandExecException {

    public SetupSshKeyException(IOException e, URI key) {
        super("Setup ssh key error", e);
        addContextValue("key", key);
    }

}
