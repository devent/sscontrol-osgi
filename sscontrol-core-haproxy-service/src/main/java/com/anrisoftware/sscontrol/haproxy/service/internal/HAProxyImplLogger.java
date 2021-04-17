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
package com.anrisoftware.sscontrol.haproxy.service.internal;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy;
import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;

/**
 * Logging for {@link HAProxyImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HAProxyImplLogger extends AbstractLogger {

    enum m {

        exportAdded("Export {} added to {}"),

        hostAdded("Host {} added to {}");

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
     * Sets the context of the logger to {@link HAProxyImpl}.
     */
    public HAProxyImplLogger() {
        super(HAProxyImpl.class);
    }

    void exportAdded(HAProxyImpl nfs, Proxy export) {
        debug(m.exportAdded, export, nfs);
    }

    void hostAdded(HAProxyImpl nfs, Frontend host) {
        debug(m.hostAdded, host, nfs);
    }

}
