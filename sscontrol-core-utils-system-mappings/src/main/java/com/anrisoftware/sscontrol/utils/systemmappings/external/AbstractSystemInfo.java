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
package com.anrisoftware.sscontrol.utils.systemmappings.external;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public abstract class AbstractSystemInfo implements SystemInfo {

    private String system;

    private String name;

    private String version;

    protected AbstractSystemInfo(String system, String name, String version) {
        this.system = system;
        this.name = name;
        this.version = version;
    }

    protected AbstractSystemInfo(SystemInfo system) {
        this(system.getSystem(), system.getName(), system.getVersion());
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String getSystem() {
        return system;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        this.version = version;
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
        if (!(obj instanceof SystemInfo)) {
            return false;
        }
        SystemInfo rhs = (SystemInfo) obj;
        boolean name = getName().equals(rhs.getName());
        boolean system = getSystem().equals(rhs.getSystem());
        boolean version = getVersion().equals(rhs.getVersion());
        return system && name && version;
    }
}
