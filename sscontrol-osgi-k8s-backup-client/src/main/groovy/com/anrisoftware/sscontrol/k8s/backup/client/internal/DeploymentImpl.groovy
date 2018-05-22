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
package com.anrisoftware.sscontrol.k8s.backup.client.internal

import static java.nio.charset.StandardCharsets.*

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.client.external.ErrorScalingDeployException
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service
import com.anrisoftware.sscontrol.k8s.backup.client.external.WaitScalingTimeoutException
import com.anrisoftware.sscontrol.k8s.backup.client.external.WaitScalingUnexpectedException
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.google.inject.assistedinject.Assisted

import groovy.json.JsonSlurper

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class DeploymentImpl implements Deployment {

	@Inject
	transient DeploymentImplLogger log

	private final ClusterHost host

	private final def kubectl

	private final Service service

	@Inject
	DeploymentImpl(@Assisted ClusterHost host, @Assisted Object kubectl, @Assisted Service service) {
		this.host = host
		this.kubectl = kubectl
		this.service = service
	}

	@Override
	Service getService() {
		return service
	}

	@Override
	int getReplicas() {
		def command = "-n ${service.namespace} get deploy ${service.name} -o jsonpath='{.spec.replicas}'"
		def ret = kubectl.runKubectl args: command, outString: true
		String output = ret[0].out
		int replicas = Integer.parseInt(output)
		log.replicasReturned this, replicas
		return replicas
	}

	@Override
	void scaleDeploy(int replicas) {
		def command = "-n ${service.namespace} scale deploy ${service.name} --replicas=${replicas}"
		kubectl.runKubectl args: command
		log.deploymentScaled this, replicas
	}

	@Override
	int getReadyReplicas() {
		def command = "-n ${service.namespace} get pods -l app=${service.name} --no-headers"
		def ret = kubectl.runKubectl args: command, outString: true
		String output = ret[0].out
		if (output.trim().isEmpty()) {
			return 0
		}
		String[] pods = StringUtils.split(output, "\n")
		int replicas = pods.inject(0) { int result, String pod ->
			String[] podProperties = StringUtils.split(pod)
			String[] readyReplicas = StringUtils.split(podProperties[1], "/")
			int ready = Integer.parseInt(readyReplicas[0])
			int containers = Integer.parseInt(readyReplicas[1])
			if (ready == containers) {
				return result + 1
			} else {
				return result
			}
		}
		log.replicasReturned this, replicas
		return replicas
	}

	@Override
	void waitScaleDeploy(int replicas, Duration timeout) {
		scaleDeploy replicas
		def executor = Executors.newSingleThreadExecutor()
		def canceled = false
		def future = executor.submit([call: {
				while (!canceled) {
					int current = readyReplicas
					if (current == replicas) {
						return true
					}
					Thread.sleep 5000
				}
				return false
			}] as Callable)

		try {
			boolean result = future.get(timeout.standardSeconds, TimeUnit.SECONDS)
			log.deploymentScaled this, replicas
			if (!result) {
				throw new ErrorScalingDeployException(service, replicas)
			}
		}
		catch (TimeoutException e) {
			throw new WaitScalingTimeoutException(service, replicas, timeout)
		}
		catch (e) {
			throw new WaitScalingUnexpectedException(e, service, replicas)
		}
		finally {
			executor.shutdownNow()
		}
	}

	@Override
	void waitExposeDeploy(String name) {
		if (getNodePort(name) != null) {
			return
		}
		def command = "-n ${service.namespace} expose deploy ${service.name} --name=${name} --type=NodePort"
		kubectl.runKubectl args: command
		log.deployExposed this, name
	}

	@Override
	Integer getNodePort(String name) {
		def command = "-n ${service.namespace} get svc -l app=${service.name} -o json"
		def ret = kubectl.runKubectl args: command, outString: true
		String output = ret[0].out
		def jsonSlurper = new JsonSlurper()
		def out = jsonSlurper.parseText(ret[0].out)
		if (out.items.size == 0) {
			return null
		}
		int port = out.items[0].spec.ports[0].nodePort
		log.nodePortReturned this, name, port
		return port
	}

	@Override
	void deleteService(String name) {
		kubectl.deleteResource namespace: service.namespace, type: "svc", name: name, checkExists: true
		log.serviceDeleted this, name
	}

	@Override
	List<String> getPods() {
		def command = "-n ${service.namespace} get pods -l app=${service.name} --no-headers  -o name"
		def ret = kubectl.runKubectl args: command, outString: true
		def names = ret[0].out.split("\n").inject([]) { List result, String name ->
			def podName = name.split("\\/")
			result << podName[1]
		}
		log.podsListed this, names
		return names
	}

	@Override
	void execCommand(String... cmd) {
		def pod = pods[0]
		def command = "-n ${service.namespace} exec ${pod} -- ${cmd.join(' ')}"
		kubectl.runKubectl args: command
		log.commandExecuted(this, pod, Arrays.toString(cmd))
	}

	@Override
	String toString() {
		ToStringBuilder.reflectionToString(this)
	}
}
