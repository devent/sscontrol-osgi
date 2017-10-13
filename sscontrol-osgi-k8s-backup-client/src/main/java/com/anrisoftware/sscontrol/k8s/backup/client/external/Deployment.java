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
import java.util.List;

import com.anrisoftware.sscontrol.types.cluster.external.Credentials;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Deployment {

    Deployment createClient();

    Object getDeployment(String namespace, String name);

    Object getService(String namespace, String name);

    void scaleDeployment(Object deploy, int replicas);

    void scaleDeployment(Object deploy, int replicas, boolean deleteOnError);

    void waitScaleUp(Object deploy, boolean deleteOnError);

    void waitScaleZero(Object deployOp);

    List<?> getPods(String namespace, String name);

    Object createPublicService(Object deploy);

    void deleteService(Object service);

    Object buildConfig(URI hostUrl, Credentials credentials);

    List<?> waitDeploy(Object deploy, int replicas, boolean ready);

    void execCommand(Object deployOp, String... cmd);
}
