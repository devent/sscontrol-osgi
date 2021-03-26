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
package com.anrisoftware.sscontrol.nfs.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.nfs.service.external.Host;
import com.google.inject.assistedinject.Assisted;

/**
 * Remote host and options.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class HostImpl implements Host {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface HostImplFactory {

        Host create(Map<String, Object> args);
    }

    private String name;

    private String options;

    @Inject
    HostImpl(@Assisted Map<String, Object> args) {
        parseName(args);
        parseOptions(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        this.name = v.toString();
    }

    private void parseOptions(Map<String, Object> args) {
        Object v = args.get("options");
        if (v != null) {
            this.options = v.toString();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
