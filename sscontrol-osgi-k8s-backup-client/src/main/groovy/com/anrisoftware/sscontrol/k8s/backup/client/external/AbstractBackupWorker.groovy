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

    Integer oldScale

    abstract ClusterService getService()

    abstract Deployment getDeploy()

    @Override
    void init() {
        deploy.with {
            this.rsyncDeploy = getDeployment service.service.namespace, "rsync-${service.service.name}"
            this.serviceDeploy = getDeployment service.service.namespace, service.service.name
            this.oldScale = getReplicas serviceDeploy
            scaleRsync rsyncDeploy, 1
            this.rsyncService = createPublicService rsyncDeploy
            this.rsyncPort = rsyncService.spec.ports[0].nodePort
        }
    }

    @Override
    void before() {
        if (oldScale) {
            deploy.scaleDeployment serviceDeploy, 0
        }
    }

    @Override
    void start(def client) {
        client(rsyncPort: rsyncPort)
    }

    @Override
    void after() {
        if (oldScale) {
            deploy.scaleDeployment serviceDeploy, oldScale, false
        }
    }

    @Override
    void finally1() {
        deploy.with {
            deleteService rsyncService
            scaleDeployment rsyncDeploy, 0
        }
    }

    Integer getReplicas(def deployOps) {
        def deploy = deployOps.get()
        deploy == null ? null : deploy.spec.replicas
    }

    def scaleRsync(def rsyncDeploy, int replicas) {
        deploy.with {
            try {
                scaleDeployment rsyncDeploy, replicas
            } catch (e) {
                scaleDeployment rsyncDeploy, replicas
            }
        }
    }
}
