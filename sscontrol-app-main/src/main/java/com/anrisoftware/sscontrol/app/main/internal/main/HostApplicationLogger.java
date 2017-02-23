/*
 * Copyright 2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.app.main.internal.main;

import static com.anrisoftware.sscontrol.app.main.internal.main.HostApplicationLogger._.felixStarted;
import static com.anrisoftware.sscontrol.app.main.internal.main.HostApplicationLogger._.felixStopped;

import java.util.Map;

import javax.inject.Singleton;

import org.apache.felix.framework.Felix;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link HostApplication}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostApplicationLogger extends AbstractLogger {

    enum _ {

        felixStarted("Felix started with {}"),

        felixStopped("Felix stopped: {}");

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
     * Sets the context of the logger to {@link HostApplication}.
     */
    public HostApplicationLogger() {
        super(HostApplication.class);
    }

    void felixStarted(Map<String, Object> configMap) {
        debug(felixStarted, configMap);
    }

    void felixStopped(Felix felix) {
        debug(felixStopped, felix);
    }
}
