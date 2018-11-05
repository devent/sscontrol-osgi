package com.anrisoftware.sscontrol.haproxy.service.internal;

/*-
 * #%L
 * sscontrol-osgi - collectd-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.haproxy.service.external.Backend;
import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;

/**
 * Logging for {@link ProxyImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ProxyImplLogger extends AbstractLogger {

    enum m {

        frontendSet("Frontend {} set to {}"),

        backendSet("Backend {} set to {}");

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
     * Sets the context of the logger to {@link ExportsImpl}.
     */
    public ProxyImplLogger() {
        super(ProxyImpl.class);
    }

    void frontendSet(ProxyImpl nfs, Frontend frontend) {
        debug(m.frontendSet, frontend, nfs);
    }

    void backendSet(ProxyImpl nfs, Backend backend) {
        debug(m.backendSet, backend, nfs);
    }

}