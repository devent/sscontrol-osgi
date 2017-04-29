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
package com.anrisoftware.sscontrol.utils.systemmappings.external;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractSystemInfo implements SystemInfo {

    private final String system;

    private final String name;

    private final String version;

    protected AbstractSystemInfo(String system, String name, String version) {
        this.system = system;
        this.name = name;
        this.version = version;
    }

    protected AbstractSystemInfo(SystemInfo system) {
        this(system.getSystem(), system.getName(), system.getVersion());
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("system", getSystem())
                .append("name", getName()).append("version", getVersion())
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getSystem()).append(getName())
                .append(getVersion()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SystemInfo rhs = (SystemInfo) obj;
        boolean name = getName().equalsIgnoreCase(rhs.getName());
        boolean system = getSystem() == null ? true
                : getSystem().equalsIgnoreCase(rhs.getSystem());
        boolean version = getVersion().equalsIgnoreCase(rhs.getVersion());
        boolean s;
        if (!name) {
            s = system;
        } else {
            s = name;
        }
        return s && version;
    }
}
