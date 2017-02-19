package com.anrisoftware.sscontrol.app.main.internal.args;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ParseArgsException extends AppException {

    public ParseArgsException(CmdLineException e, String[] args) {
        super("Error parsing arguments", e);
        addContextValue("args", args);
    }

}
