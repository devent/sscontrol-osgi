package com.anrisoftware.sscontrol.k8s.restore.script.internal.script_1_7;

import com.anrisoftware.sscontrol.k8s.backup.client.external.BackupWorker;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface RestoreWorkerImplFactory {

    BackupWorker create(Restore service, Deployment deployment);
}
