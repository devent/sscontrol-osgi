package com.anrisoftware.sscontrol.command.shell.external.template;

import java.util.Map;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WriteTemplateException extends AppException {

    public WriteTemplateException(Exception e, Map<String, Object> args) {
        super("Error write template", e);
        addContextValue("args", args);
    }

}
