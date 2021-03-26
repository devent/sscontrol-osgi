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
package com.anrisoftware.sscontrol.collectd.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.collectd.service.external.Config;
import com.google.inject.assistedinject.Assisted;

/**
 * Collectd configuration.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ConfigImpl implements Config {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ConfigImplFactory {

        Config create(Map<String, Object> args);
    }

    private final String name;

    private String script;

    @Inject
    ConfigImpl(@Assisted Map<String, Object> args) {
        Object v = args.get("name");
        this.name = v.toString();
        v = args.get("script");
        setScript(v.toString());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
