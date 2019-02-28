package com.anrisoftware.sscontrol.haproxy.service.internal;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.haproxy.service.external.Target;

/**
 * Proxy target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AbstractTarget implements Target {

    private String name;

    private String address;

    private Integer port;

    protected AbstractTarget(Map<String, Object> args) {
        parseName(args);
        parseAddress(args);
        parsePort(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            this.name = v.toString();
        }
    }

    private void parseAddress(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            this.address = v.toString();
        }
    }

    private void parsePort(Map<String, Object> args) {
        Object v = args.get("port");
        if (v != null) {
            this.port = ((Number) v).intValue();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
