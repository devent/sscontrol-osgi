package com.anrisoftware.sscontrol.k8s.backup.client.external;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CreateClientException extends BackupClientException {

    public CreateClientException(Deployment deployment, Exception cause) {
        super("Create client error", cause);
        addContextValue("deployment", deployment);
    }
}
