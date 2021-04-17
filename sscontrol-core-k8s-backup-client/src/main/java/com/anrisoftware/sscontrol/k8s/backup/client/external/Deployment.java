/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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

import java.util.List;

import org.joda.time.Duration;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public interface Deployment {

    Service getService();

    int getReplicas();

    int getReadyReplicas();

    void scaleDeploy(int replicas);

    void waitScaleDeploy(int replicas, Duration timeout);

    void waitExposeDeploy(String name);

    /**
     * Returns the exposed node port for a service.
     *
     * @return the {@link Integer} node port or <code>null</code> if the service
     *         was not exposed.
     */
    Integer getNodePort(String name);

    void deleteService(String name);

    List<String> getPods();

    void execCommand(String... cmd);
}
