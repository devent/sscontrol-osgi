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
package com.anrisoftware.sscontrol.k8snode.internal;

import static com.anrisoftware.sscontrol.k8snode.internal.K8sNodeImplLogger._.masterSet;
import static com.anrisoftware.sscontrol.k8snode.internal.K8sNodeImplLogger._.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8snode.external.Master;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link K8sNodeImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class K8sNodeImplLogger extends AbstractLogger {

    enum _ {

        tlsSet("TLS {} set for {}"),

        masterSet("Master {} set for {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link K8sNodeImpl}.
     */
    public K8sNodeImplLogger() {
        super(K8sNodeImpl.class);
    }

    void tlsSet(K8sNodeImpl k8s, Tls tls) {
        debug(tlsSet, tls, k8s);
    }

    void masterSet(K8sNodeImpl k8s, Master master) {
        debug(masterSet, master, k8s);
    }

}
