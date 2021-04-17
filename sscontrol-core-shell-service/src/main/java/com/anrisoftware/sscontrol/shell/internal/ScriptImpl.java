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
package com.anrisoftware.sscontrol.shell.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.shell.external.Script;
import com.google.inject.assistedinject.Assisted;

/**
 * Script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ScriptImpl implements Script {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ScriptImplFactory {

        Script create(Map<String, Object> args);
    }

    private final Map<String, Object> vars;

    @Inject
    ScriptImpl(@Assisted Map<String, Object> args) {
        this.vars = new HashMap<>(args);
    }

    @Override
    public Map<String, Object> getVars() {
        return vars;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vars", getVars()).toString();
    }

}
