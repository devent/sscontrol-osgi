package com.anrisoftware.sscontrol.k8sbase.base.internal;

import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.advertiseAddressSet;
import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.apiServersAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.dnsAddressSet;
import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.hostnameOverrideSet;
import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.podRangeSet;
import static com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImplLogger.m.serviceRangeSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImpl;

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

        dnsAddressSet("DNS address {} set for {}"),

        apiServersAdded("API server {} added to {}"),

        serviceRangeSet("Service range {} set for {}"),

        hostnameOverrideSet("Hostname override {} set for {}"),

        podRangeSet("Pod range {} set for {}");

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

    void advertiseAddressSet(ClusterImpl cluster, String address) {
        debug(advertiseAddressSet, address, cluster);
    }

    void dnsAddressSet(ClusterImpl cluster, String address) {
        debug(dnsAddressSet, address, cluster);
    }

    void apiServersAdded(ClusterImpl cluster, String server) {
        debug(apiServersAdded, server, cluster);
    }

    void serviceRangeSet(ClusterImpl cluster, String range) {
        debug(serviceRangeSet, range, cluster);
    }

    void hostnameOverrideSet(ClusterImpl cluster, String hostname) {
        debug(hostnameOverrideSet, hostname, cluster);
    }

    void podRangeSet(ClusterImpl cluster, String range) {
        debug(podRangeSet, range, cluster);
    }
}
