/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.internal;

import static com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterImplLogger._.groupSet;
import static com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterImplLogger._.hostAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.external.SshHost;

/**
 * Logging for {@link K8sMasterImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class K8sMasterImplLogger extends AbstractLogger {

    enum _ {

        hostAdded("Host added {} to {}"),

        groupSet("Group '{}' set for {}");

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
     * Sets the context of the logger to {@link K8sMasterImpl}.
     */
    public K8sMasterImplLogger() {
        super(K8sMasterImpl.class);
    }

    void hostAdded(K8sMasterImpl ssh, SshHost host) {
        debug(hostAdded, host, ssh);
    }

    void groupSet(K8sMasterImpl ssh, String group) {
        debug(groupSet, group, ssh);
    }
}
