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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.preferredTypesAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link KubeletImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class KubeletImplLogger extends AbstractLogger {

    enum m {

        tlsSet("TLS {} set for {}"),

        preferredTypesAdded("Preferred node address types {} added to {}"),

        clientSet("Client {} set for {}"),

        portSet("Port {} set for {}"),

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
     * Sets the context of the logger to {@link KubeletImpl}.
     */
    public KubeletImplLogger() {
        super(KubeletImpl.class);
    }

    void tlsSet(KubeletImpl kubelet, Tls tls) {
        debug(tlsSet, tls, kubelet);
    }

    void preferredTypesAdded(KubeletImpl kubelet, Object v) {
        debug(preferredTypesAdded, v, kubelet);
    }

    void clientSet(KubeletImpl kubelet, Tls client) {
        debug(clientSet, client, kubelet);
    }

    void portSet(KubeletImpl kubelet, int port) {
        debug(m.portSet, port, kubelet);
    }

    void addressSet(KubeletImpl kubelet, String address) {
        debug(m.addressSet, address, kubelet);
    }

}
