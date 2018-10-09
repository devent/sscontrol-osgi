package com.anrisoftware.sscontrol.k8s.backup.client.external;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Service for backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractService implements Service {

    public static Service createService(Map<String, Object> args) {
        return new AbstractService(args) {
        };
    }

    private String name;

    private String namespace;

    protected AbstractService(Map<String, Object> args) {
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseName(args);
        parseNamespace(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name=null", v, notNullValue());
        setName(v.toString());
    }

    private void parseNamespace(Map<String, Object> args) {
        Object v = args.get("namespace");
        assertThat("namespace=null", v, notNullValue());
        setNamespace(v.toString());
    }

}
