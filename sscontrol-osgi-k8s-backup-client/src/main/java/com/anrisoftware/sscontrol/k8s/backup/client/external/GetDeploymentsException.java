package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GetDeploymentsException extends BackupClientException {

    public GetDeploymentsException(ScriptBase script, String namespace,
            String name, Exception cause) {
        super("Get deployments error", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("script", script);
    }
}
