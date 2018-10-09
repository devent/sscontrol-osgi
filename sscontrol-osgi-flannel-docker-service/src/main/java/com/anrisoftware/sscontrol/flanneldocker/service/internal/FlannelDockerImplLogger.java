package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImplLogger.a.backendSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImplLogger.a.bindingSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImplLogger.a.etcdSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImplLogger.a.networkSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImplLogger.a.nodeAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Binding;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Etcd;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Network;

/**
 * Logging for {@link FlannelDockerImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class FlannelDockerImplLogger extends AbstractLogger {

    enum a {

        etcdSet("Etcd {} set for {}"),

        networkSet("Network {} set for {}"),

        backendSet("Backend {} set for {}"),

        bindingSet("Binding {} set for {}"),

        nodeAdded("Node {} added to {}");

        private String name;

        private a(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link FlannelDockerImpl}.
     */
    public FlannelDockerImplLogger() {
        super(FlannelDockerImpl.class);
    }

    void etcdSet(FlannelDockerImpl flannel, Etcd etcd) {
        debug(etcdSet, etcd, flannel);
    }

    void networkSet(FlannelDockerImpl flannel, Network network) {
        debug(networkSet, network, flannel);
    }

    void backendSet(FlannelDockerImpl flannel, Backend backend) {
        debug(backendSet, backend, flannel);
    }

    void bindingSet(FlannelDockerImpl flannel, Binding binding) {
        debug(bindingSet, binding, flannel);
    }

    void nodeAdded(FlannelDockerImpl flannel, Object node) {
        debug(nodeAdded, node, flannel);
    }
}
