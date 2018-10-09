package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GetReplicasDeploymentException extends BackupClientException {

    public GetReplicasDeploymentException(ScriptBase script, String namespace,
            String name, Exception cause) {
        super("Get replicas error", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("script", script);
    }
}
