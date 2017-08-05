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
public class GetDeploymentsException extends AppException {

    public GetDeploymentsException(ScriptBase script, String namespace,
            String name, Exception cause) {
        super("Get deployments error", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("script", script);
    }
}
