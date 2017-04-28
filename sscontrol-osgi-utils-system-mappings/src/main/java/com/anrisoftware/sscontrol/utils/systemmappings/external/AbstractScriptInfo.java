package com.anrisoftware.sscontrol.utils.systemmappings.external;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractScriptInfo extends AbstractSystemInfo
        implements ScriptInfo {

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("service", getService())
                .appendSuper(super.toString()).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(getService()).hashCode();
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
        ScriptInfo rhs = (ScriptInfo) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(getService(), rhs.getService()).isEquals();
    }
}
