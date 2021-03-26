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
package com.anrisoftware.sscontrol.runner.groovy.internal;

import static com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImplLogger.m.scriptNotFound;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;

/**
 * Logging for {@link RunScriptImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RunScriptImplLogger extends AbstractLogger {

    enum m {

        scriptNotFound("Service '{}' script not found for '{}'");

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
     * Sets the context of the logger to {@link RunScriptImpl}.
     */
    public RunScriptImplLogger() {
        super(RunScriptImpl.class);
    }

    void scriptNotFound(String name, ScriptInfo system) {
        debug(scriptNotFound, name, system);
    }
}
