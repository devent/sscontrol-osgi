package com.anrisoftware.sscontrol.types.misc.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.misc.external.UserPassword;

/**
 * User name and password credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class UserPasswordImpl implements UserPassword {

    private final String name;

    private final String password;

    public UserPasswordImpl() {
        this(null, null);
    }

    public UserPasswordImpl(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UserPassword changeName(String name) {
        return new UserPasswordImpl(name, password);
    }

    @Override
    public UserPassword changePassword(String password) {
        return new UserPasswordImpl(name, password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("password", password).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(password).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserPasswordImpl)) {
            return false;
        }
        UserPasswordImpl rhs = (UserPasswordImpl) obj;
        return new EqualsBuilder().append(name, rhs.name)
                .append(password, rhs.password).isEquals();
    }
}
