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

import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImplLogger.m.advertiseAddressSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImplLogger.m.apiServersAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImplLogger.m.dnsDomainSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImplLogger.m.podRangeSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImplLogger.m.serviceRangeSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ClusterImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ClusterImplLogger extends AbstractLogger {

    enum m {

        advertiseAddressSet("Advertise address {} set for {}"),

        dnsDomainSet("DNS domain {} set for {}"),

        apiServersAdded("API server {} added to {}"),

        serviceRangeSet("Service range {} set for {}"),

        podRangeSet("Pod range {} set for {}"),

        hostsSet("Kubectl hosts {} set for {}"),

        hostAdded("Kubectl host {} added for {}");

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
     * Sets the context of the logger to {@link ClusterImpl}.
     */
    public ClusterImplLogger() {
        super(ClusterImpl.class);
    }

    void advertiseAddressSet(ClusterImpl cluster, Object address) {
        debug(advertiseAddressSet, address, cluster);
    }

    void dnsDomainSet(ClusterImpl cluster, String address) {
        debug(dnsDomainSet, address, cluster);
    }

    void apiServersAdded(ClusterImpl cluster, Object server) {
        debug(apiServersAdded, server, cluster);
    }

    void serviceRangeSet(ClusterImpl cluster, String range) {
        debug(serviceRangeSet, range, cluster);
    }

    void podRangeSet(ClusterImpl cluster, String range) {
        debug(podRangeSet, range, cluster);
    }

    void hostsSet(ClusterImpl cluster, Object target) {
        debug(m.hostsSet, target, cluster);
    }

    void hostAdded(ClusterImpl cluster, Object target) {
        debug(m.hostAdded, target, cluster);
    }
}
