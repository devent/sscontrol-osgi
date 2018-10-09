package com.anrisoftware.sscontrol.hosts.service.internal;

import static com.anrisoftware.sscontrol.hosts.service.internal.HostsServiceImpl.HOSTS_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.hosts.service.external.Host;
import com.anrisoftware.sscontrol.hosts.service.external.Hosts;
import com.anrisoftware.sscontrol.hosts.service.external.HostsService;
import com.anrisoftware.sscontrol.hosts.service.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostsImpl implements Hosts {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostsImplFactory extends HostsService {

    }

    private final HostsImplLogger log;

    private final List<Host> hosts;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final HostImplFactory hostFactory;

    @AssistedInject
    HostsImpl(HostsImplLogger log, HostImplFactory hostFactory,
            HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.hostFactory = hostFactory;
        this.targets = new ArrayList<>();
        this.hosts = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    public void ip(Map<String, Object> args, String address) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("ip", address);
        ip(a);
    }

    public void ip(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        List<String> aliases = new ArrayList<>();
        a.put("address", a.get("ip"));
        a.put("aliases", aliases);
        Object v = a.get("alias");
        if (v != null) {
            if (v instanceof String) {
                aliases.addAll(asList(split(v.toString(), ',')));
            } else {
                aliases.addAll(ToList.<String>toList(args.get("alias")));
            }
            a.put("aliases", aliases);
        }
        a.put("identifier", a.get("on"));
        if (a.get("on") == null) {
            a.put("identifier", "host");
        }
        Host h = hostFactory.create(a);
        log.hostAdded(this, h);
        hosts.add(h);
    }

    @Override
    public String getName() {
        return HOSTS_NAME;
    }

    @Override
    public List<Host> getHosts() {
        return hosts;
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
                .append("hosts", hosts).append("targets", targets).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
        if (args.get("ip") != null) {
            ip(args);
        }
    }

}
