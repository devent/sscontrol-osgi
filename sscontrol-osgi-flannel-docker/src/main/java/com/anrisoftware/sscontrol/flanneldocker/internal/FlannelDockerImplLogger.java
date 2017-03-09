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
package com.anrisoftware.sscontrol.flanneldocker.internal;

import static com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImplLogger.a.backendSet;
import static com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImplLogger.a.bindingSet;
import static com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImplLogger.a.etcdSet;
import static com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImplLogger.a.networkSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.flanneldocker.external.Backend;
import com.anrisoftware.sscontrol.flanneldocker.external.Binding;
import com.anrisoftware.sscontrol.flanneldocker.external.Etcd;
import com.anrisoftware.sscontrol.flanneldocker.external.Network;

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

        bindingSet("Binding {} set for {}");

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
}
