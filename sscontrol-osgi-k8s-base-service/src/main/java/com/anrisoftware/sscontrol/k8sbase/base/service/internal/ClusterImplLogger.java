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
