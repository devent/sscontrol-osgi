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
package com.anrisoftware.sscontrol.k8scluster.service.internal;

import static com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterImplLogger.m.clusterSet;
import static com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterImplLogger.m.contextSet;
import static com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterImplLogger.m.credentialsAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8scluster.service.external.Cluster;
import com.anrisoftware.sscontrol.k8scluster.service.external.Context;
import com.anrisoftware.sscontrol.types.cluster.external.Credentials;

/**
 * Logging for {@link K8sClusterImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class K8sClusterImplLogger extends AbstractLogger {

    enum m {

        clusterSet("Cluster {} set for {}"),

        credentialsAdded("Credentials {} added for {}"),

        contextSet("Context {} set for {}");

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
     * Sets the context of the logger to {@link K8sClusterImpl}.
     */
    public K8sClusterImplLogger() {
        super(K8sClusterImpl.class);
    }

    void credentialsAdded(K8sClusterImpl k8s, Credentials auth) {
        debug(credentialsAdded, auth, k8s);
    }

    void clusterSet(K8sClusterImpl k8s, Cluster cluster) {
        debug(clusterSet, cluster, k8s);
    }

    void contextSet(K8sClusterImpl k8s, Context context) {
        debug(contextSet, context, k8s);
    }
}
