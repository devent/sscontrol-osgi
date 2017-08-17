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
