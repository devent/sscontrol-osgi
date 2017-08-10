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
package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.internal.DeploymentImpl;

import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation;

/**
 * Logging for {@link DeploymentImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DeploymentLogger extends AbstractLogger {

    enum m {

        createdClient("Client {} created."),

        scaledDeployment("Deployment scaled to {}: {}"),

        createPublicService("Public service {}/{} created."),

        deleteService("Service {}/{} deleted.");

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
    DeploymentLogger() {
        super(DeploymentImpl.class);
    }

    void createdClient(URI url) {
        debug(m.createdClient, url);
    }

    @SuppressWarnings("rawtypes")
    void scaledDeployment(HasMetadataOperation deploy, int replicas) {
        debug(m.scaledDeployment, replicas, deploy);
    }

    void createPublicService(String namespace, String name) {
        debug(m.createPublicService, namespace, name);
    }

    void deleteService(String namespace, String name) {
        debug(m.deleteService, namespace, name);
    }
}
