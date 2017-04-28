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
        boolean system = getSystem().equalsIgnoreCase(rhs.getSystem());
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
