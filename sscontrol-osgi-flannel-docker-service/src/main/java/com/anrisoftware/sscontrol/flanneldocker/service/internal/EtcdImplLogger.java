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
package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.addressSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.endpointAdded;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.endpointsAdded;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.tlsSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link EtcdImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class EtcdImplLogger extends AbstractLogger {

    enum m {

        tlsSet("TLS {} set for {}"),

        endpointAdded("Endpoint {} added to {}"),

        endpointsAdded("Endpoints {} added to {}"),

        addressSet("Address {} set for {}");

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
     * Sets the context of the logger to {@link EtcdImpl}.
     */
    public EtcdImplLogger() {
        super(EtcdImpl.class);
    }

    void tlsSet(EtcdImpl etcd, Tls tls) {
        debug(tlsSet, tls, etcd);
    }

    void endpointAdded(EtcdImpl etcd, Object endpoint) {
        debug(endpointAdded, endpoint, etcd);
    }

    void endpointsAdded(EtcdImpl etcd, List<?> list) {
        debug(endpointsAdded, list, etcd);
    }

    void addressSet(EtcdImpl etcd, Object address) {
        debug(addressSet, address, etcd);
    }
}
