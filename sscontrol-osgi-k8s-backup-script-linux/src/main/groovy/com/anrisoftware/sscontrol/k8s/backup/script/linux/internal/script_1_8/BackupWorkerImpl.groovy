package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_8

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractBackupWorker
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService
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
    ScriptBase script

    @Inject
    @Assisted
    ClusterService cluster

    @Inject
    @Assisted("deploy")
    Deployment deploy

    @Inject
    @Assisted("rsync")
    Deployment rsync

    Duration getScaleTimeout() {
        script.timeoutLong
    }
}
