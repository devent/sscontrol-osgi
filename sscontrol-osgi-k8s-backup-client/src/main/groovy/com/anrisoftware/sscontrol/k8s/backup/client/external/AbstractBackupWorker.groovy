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
package com.anrisoftware.sscontrol.k8s.backup.client.external

import static org.hamcrest.Matchers.*

import com.anrisoftware.sscontrol.types.cluster.external.ClusterService

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractBackupWorker implements BackupWorker {

    def rsyncDeploy

    def serviceDeploy

    def rsyncService

    int rsyncPort

    int oldReplicasCount

    /**
     * Returns the cluster service for kubectl.
     *
     * @return {@link ClusterService}
     */
    abstract ClusterService getCluster()

    /**
     * Returns the deployment to backup.
     *
     * @return {@link Deployment}
     */
    abstract Deployment getDeploy()

    /**
     * Returns the rsync deployment.
     *
     * @return {@link Deployment}
     */
    abstract Deployment getRsync()

    @Override
    void init() {
        deploy.with { this.oldReplicasCount = replicas }
        rsync.with {
            waitScaleDeploy 1
            this.rsyncService = waitExposeDeploy "rsync-service"
            this.rsyncPort = getNodePort "rsync-service"
        }
    }

    @Override
    void before() {
        if (oldReplicasCount) {
            deploy.with { waitScaleDeploy 0 }
        }
    }

    @Override
    void start(def client) {
        client(rsyncPort: rsyncPort)
    }

    @Override
    void after() {
        if (oldReplicasCount) {
            deploy.with { waitScaleDeploy oldReplicasCount }
        }
    }

    @Override
    void finally1() {
        rsync.with {
            deleteService "rsync-service"
            waitScaleDeploy 0
        }
    }
}
