package com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;
import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CreateClientException extends AppException {

    public CreateClientException(ScriptBase script, Exception cause) {
        super("Create client error", cause);
        addContextValue("script", script);
    }
}
