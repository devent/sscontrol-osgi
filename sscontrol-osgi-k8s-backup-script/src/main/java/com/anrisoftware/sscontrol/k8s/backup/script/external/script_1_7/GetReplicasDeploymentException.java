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
public class GetReplicasDeploymentException extends AppException {

    public GetReplicasDeploymentException(ScriptBase script, String namespace,
            String name, Exception cause) {
        super("Get replicas error", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("script", script);
    }
}
