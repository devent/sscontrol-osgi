package com.anrisoftware.sscontrol.zimbra.service.internal;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.zimbra.service.external.Domain;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Zimbra service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DomainImpl implements Domain {

    /**
     *
     * @author Erwin Müller, erwin.mueller@deventm.de
     * @since 1.0
     */
    public interface DomainImplFactory {

        Domain create();

        Domain create(Map<String, Object> args);

    }

    private String name;

    private String email;

    @AssistedInject
    DomainImpl() {
    }

    @AssistedInject
    DomainImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseName(args);
        parseEmail(args);
    }

    private void parseEmail(Map<String, Object> args) {
        Object v = args.get("email");
        setEmail(v.toString());
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
    }

}
