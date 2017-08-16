package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface BackupWorkerFactory {

    BackupWorker create(Backup service, Deployment deployment);
}
