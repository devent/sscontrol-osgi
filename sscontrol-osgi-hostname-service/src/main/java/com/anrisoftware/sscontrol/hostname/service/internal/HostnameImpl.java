package com.anrisoftware.sscontrol.hostname.service.internal;

import static com.anrisoftware.sscontrol.hostname.service.internal.HostnameServiceImpl.HOSTNAME_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hostname.service.external.Hostname;
import com.anrisoftware.sscontrol.hostname.service.external.HostnameService;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Hostname service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostnameImpl implements Hostname {

    public interface HostnameImplFactory extends HostnameService {

    }

    private final HostnameImplLogger log;

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private String hostname;

    @AssistedInject
    HostnameImpl(HostnameImplLogger log,
            HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<TargetHost>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    public void set(Map<String, Object> args) {
        parseArgs(args);
    }

    public void setFqdn(String fqdn) {
        this.hostname = fqdn;
        log.hostnameSet(this, fqdn);
    }

    @Override
    public String getName() {
        return HOSTNAME_NAME;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hostname", hostname).append("hosts", targets)
                .toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
        v = args.get("fqdn");
        if (v != null) {
            setFqdn(v.toString());
        }
    }

}
