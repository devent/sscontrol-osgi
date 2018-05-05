/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractBackupWorker
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService
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
