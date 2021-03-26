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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.allowPrivilegedSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.clusterHostsAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.clusterSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.kubeletSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.labelAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.pluginAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.taintAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImplLogger.m.tlsSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Label;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

/**
 * Logging for {@link K8sImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class K8sImplLogger extends AbstractLogger {

    enum m {

        pluginAdded("Plugin {} added to {}"),

        clusterSet("Cluster {} set for {}"),

        tlsSet("TLS {} set for {}"),

        allowPrivilegedSet("Allow privileged {} set for {}"),

        kubeletSet("Kubelet {} set for {}"),

        labelAdded("Label {} added to {}"),

        taintAdded("Taint {} added to {}"),

        clusterHostsAdded("Cluster hosts added to {}: {}");

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
     * Sets the context of the logger to {@link K8sImpl}.
     */
    public K8sImplLogger() {
        super(K8sImpl.class);
    }

    void pluginAdded(K8sImpl k8s, Plugin plugin) {
        debug(pluginAdded, plugin, k8s);
    }

    void clusterSet(K8sImpl k8s, Cluster cluster) {
        debug(clusterSet, cluster, k8s);
    }

    void tlsSet(K8sImpl k8s, Tls tls) {
        debug(tlsSet, tls, k8s);
    }

    void allowPrivilegedSet(K8sImpl k8s, boolean allow) {
        debug(allowPrivilegedSet, allow, k8s);
    }

    void kubeletSet(K8sImpl k8s, Kubelet kubelet) {
        debug(kubeletSet, kubelet, k8s);
    }

    void labelAdded(K8sImpl k8s, Label label) {
        debug(labelAdded, label, k8s);
    }

    void taintAdded(K8sImpl k8s, Taint taint) {
        debug(taintAdded, taint, k8s);
    }

    void clusterHostsAdded(K8sImpl k8s, List<ClusterHost> list) {
        debug(clusterHostsAdded, list, k8s);
    }
}
