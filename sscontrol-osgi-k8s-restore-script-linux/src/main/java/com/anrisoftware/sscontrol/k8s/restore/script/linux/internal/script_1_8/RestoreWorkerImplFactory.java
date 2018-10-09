package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;
import com.anrisoftware.sscontrol.k8s.backup.client.external.BackupWorker;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface RestoreWorkerImplFactory {

    BackupWorker create(ScriptBase script, ClusterService cluster,
            @Assisted("deploy") Deployment deploy,
            @Assisted("rsync") Deployment rsync);
}
