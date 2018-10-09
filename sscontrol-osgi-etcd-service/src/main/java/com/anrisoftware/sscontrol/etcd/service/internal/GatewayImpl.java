package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.Gateway;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> gateway.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class GatewayImpl implements Gateway {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface GatewayImplFactory {

        Gateway create(@Assisted Map<String, Object> args);
    }

    private final List<String> endpoints;

    private transient GatewayImplLogger log;

    @Inject
    GatewayImpl(GatewayImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        this.endpoints = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * endpoint << "https://etcd-0:2379"
     * </pre>
     */
    public List<String> getEndpoint() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                addEndpoint(property);
            }
        });
    }

    public void addEndpoint(String endpoint) {
        endpoints.add(endpoint);
        log.advertiseAdded(this, endpoint);
    }

    @Override
    public List<String> getEndpoints() {
        return endpoints;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseEndpoints(args);
    }

    private void parseEndpoints(Map<String, Object> args) {
        Object v = args.get("endpoints");
        if (v != null) {
            addEndpoint(v.toString());
        }
    }

}
