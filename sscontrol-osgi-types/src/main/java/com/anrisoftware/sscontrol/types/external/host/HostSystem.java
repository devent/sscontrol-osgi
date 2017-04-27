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
package com.anrisoftware.sscontrol.types.external.host;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface HostSystem {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public abstract class AbstractHostSystem implements HostSystem {

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("system", getSystem())
                    .append("name", getName()).append("version", getVersion())
                    .toString();
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this, false);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj, false);
        }
    }

    /**
     * Returns system name, for example linux, windows, macos.
     */
    String getSystem();

    /**
     * Returns the distribution name, for example debian, centos, Windows.
     */
    String getName();

    /**
     * Returns the system version, for example 8 (for Debian Jessie), 7 (for
     * Windows 7).
     */
    String getVersion();
}
