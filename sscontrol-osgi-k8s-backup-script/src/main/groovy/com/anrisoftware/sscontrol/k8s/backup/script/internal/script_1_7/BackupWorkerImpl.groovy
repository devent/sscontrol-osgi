package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractBackupWorker
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.google.inject.assistedinject.Assisted

/**
 * Starts the backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class BackupWorkerImpl extends AbstractBackupWorker {

    @Inject
    @Assisted
    Backup service

    @Inject
    @Assisted
    Deployment deploy
}
