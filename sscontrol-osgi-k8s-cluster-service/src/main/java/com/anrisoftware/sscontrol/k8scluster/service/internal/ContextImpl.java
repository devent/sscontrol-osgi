package com.anrisoftware.sscontrol.k8scluster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8scluster.service.external.Context;
import com.google.inject.assistedinject.Assisted;

/**
 * Cluster.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ContextImpl implements Context {

    private String name;

    private final ContextImplLogger log;

    @Inject
    ContextImpl(ContextImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
        log.nameSet(this, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
    }

}
