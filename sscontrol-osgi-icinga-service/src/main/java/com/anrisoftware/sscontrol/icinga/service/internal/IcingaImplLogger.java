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
package com.anrisoftware.sscontrol.icinga.service.internal;

import static com.anrisoftware.sscontrol.icinga.service.internal.IcingaImplLogger.m.configAdded;
import static com.anrisoftware.sscontrol.icinga.service.internal.IcingaImplLogger.m.featureAdded;
import static com.anrisoftware.sscontrol.icinga.service.internal.IcingaImplLogger.m.pluginAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.icinga.service.external.Config;
import com.anrisoftware.sscontrol.icinga.service.external.Feature;
import com.anrisoftware.sscontrol.icinga.service.external.Plugin;

/**
 * Logging for {@link IcingaImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class IcingaImplLogger extends AbstractLogger {

    enum m {

        pluginAdded("Plugin {} added to {}"),

        featureAdded("Feature {} added to {}"),

        configAdded("Config {} added to {}"),;

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
     * Sets the context of the logger to {@link IcingaImpl}.
     */
    public IcingaImplLogger() {
        super(IcingaImpl.class);
    }

    void pluginAdded(IcingaImpl icinga, Plugin plugin) {
        debug(pluginAdded, plugin, icinga);
    }

    void featureAdded(IcingaImpl icinga, Feature feature) {
        debug(featureAdded, feature, icinga);
    }

    void configAdded(IcingaImpl icinga, Config config) {
        debug(configAdded, config, icinga);
    }
}