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
package com.anrisoftware.sscontrol.k8s.backup.client.internal;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.internal.DeploymentImpl;

/**
 * Logging for {@link DeploymentImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DeploymentImplLogger extends AbstractLogger {

    enum m {

        replicasReturned("Replicas {} returned for {}"),

        deploymentScaled("Scaled to {}: {}"),

        deployExposed("Service {} exposed for {}."),

        nodePortReturned("Node port {} for {} returned for {}."),

        serviceDeleted("Service {} deleted for {}."),

        commandExecuted("Command executed for pod {} for {}: {}"),

        podsListed("Pods listed for {}: {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link RestoreImpl}.
     */
    DeploymentImplLogger() {
        super(DeploymentImpl.class);
    }

    void replicasReturned(DeploymentImpl deploy, int replicas) {
        debug(m.replicasReturned, replicas, deploy);
    }

    void deploymentScaled(DeploymentImpl deploy, int replicas) {
        debug(m.deploymentScaled, replicas, deploy);
    }

    void deployExposed(DeploymentImpl deploy, String name) {
        debug(m.deployExposed, name, deploy);
    }

    void nodePortReturned(DeploymentImpl deploy, String name, int port) {
        debug(m.nodePortReturned, port, name, deploy);
    }

    void serviceDeleted(DeploymentImpl deploy, String name) {
        debug(m.serviceDeleted, name, deploy);
    }

    void podsListed(DeploymentImpl deploy, List<String> pods) {
        debug(m.podsListed, deploy, pods);
    }

    void commandExecuted(DeploymentImpl deploy, String pod, String cmd) {
        debug(m.commandExecuted, pod, deploy, cmd);
    }
}
