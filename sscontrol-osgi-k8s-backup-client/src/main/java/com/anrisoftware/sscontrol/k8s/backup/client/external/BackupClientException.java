package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class BackupClientException extends AppException {

    protected BackupClientException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BackupClientException(String message) {
        super(message);
    }

}
