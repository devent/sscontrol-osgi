package com.anrisoftware.sscontrol.k8s.backup.client.external

import static org.hamcrest.Matchers.*

import org.joda.time.Duration

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

	String rsyncServiceName

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
		this.rsyncServiceName = "${rsync.service.name}-public"
		deploy.with { this.oldReplicasCount = replicas }
		if (oldReplicasCount) {
			deploy.with { waitScaleDeploy 0, scaleTimeout }
		}
	}

	@Override
	void before() {
		rsync.with {
			waitScaleDeploy 1, scaleTimeout
			this.rsyncService = waitExposeDeploy rsyncServiceName
			this.rsyncPort = getNodePort rsyncServiceName
		}
	}

	@Override
	void start(def client) {
		client(rsyncPort: rsyncPort)
	}

	@Override
	void after() {
		rsync.with {
			deleteService rsyncServiceName
			waitScaleDeploy 0, scaleTimeout
		}
	}

	@Override
	void finally1() {
		if (oldReplicasCount) {
			deploy.with { waitScaleDeploy oldReplicasCount, scaleTimeout }
		}
	}

	abstract Duration getScaleTimeout()
}
