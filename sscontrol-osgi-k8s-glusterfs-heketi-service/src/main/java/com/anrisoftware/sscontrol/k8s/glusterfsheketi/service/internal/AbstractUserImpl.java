package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.User;

/**
 * User service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class AbstractUserImpl implements User {

    private String key;

    protected AbstractUserImpl(Map<String, Object> args) {
        Object v = args.get("key");
        if (v != null) {
            this.key = v.toString();
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
