package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.google.inject.assistedinject.Assisted

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class BackupWorker {

    @Inject
    @Assisted
    Backup service

    @Inject
    @Assisted
    Deployment d

    def rsyncDeploy

    def serviceDeploy

    def rsyncService

    int rsyncPort

    Integer oldScale

    void init() {
        d.with {
            this.rsyncDeploy = getDeployment service.service.namespace, "rsync-${service.service.name}"
            this.serviceDeploy = getDeployment service.service.namespace, service.service.name
            this.oldScale = getReplicas serviceDeploy
            scaleRsync rsyncDeploy, 1
            this.rsyncService = createPublicService rsyncDeploy
            this.rsyncPort = rsyncService.spec.ports[0].nodePort
        }
    }

    void before() {
        if (oldScale) {
            d.scaleDeployment serviceDeploy, 0
        }
    }

    void start(Closure client) {
        client(rsyncPort: rsyncPort)
    }

    void after() {
        if (oldScale) {
            d.scaleDeployment serviceDeploy, oldScale, false
        }
    }

    void finally1() {
        d.with {
            deleteService rsyncService
            scaleDeployment rsyncDeploy, 0
        }
    }

    Integer getReplicas(def deployOps) {
        def deploy = deployOps.get()
        deploy == null ? null : deploy.spec.replicas
    }

    def scaleRsync(def rsyncDeploy, int replicas) {
        d.with {
            try {
                scaleDeployment rsyncDeploy, replicas
            } catch (e) {
                scaleDeployment rsyncDeploy, replicas
            }
        }
    }
}
