package com.anrisoftware.sscontrol.k8s.restore.script.internal.script_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractBackupWorker
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore
import com.google.inject.assistedinject.Assisted

/**
 * Starts the restore.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class RestoreWorkerImpl extends AbstractBackupWorker {

    @Inject
    @Assisted
    Restore service

    @Inject
    @Assisted
    Deployment deploy
}
