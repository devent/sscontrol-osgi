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

import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.FlannelPluginImplLogger.m.rangeSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.FlannelPluginImpl;

/**
 * Logging for {@link FlannelPluginImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class FlannelPluginImplLogger extends AbstractLogger {

    enum m {

        rangeSet("Range {} set for {}");

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
     * Sets the context of the logger to {@link FlannelPluginImpl}.
     */
    public FlannelPluginImplLogger() {
        super(FlannelPluginImpl.class);
    }

    void rangeSet(FlannelPluginImpl flannel, String range) {
        debug(rangeSet, range, flannel);
    }
}
