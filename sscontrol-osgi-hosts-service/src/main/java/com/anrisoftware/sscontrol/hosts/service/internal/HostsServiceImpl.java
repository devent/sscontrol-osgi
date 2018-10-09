package com.anrisoftware.sscontrol.hosts.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.hosts.service.external.Hosts;
import com.anrisoftware.sscontrol.hosts.service.external.HostsService;
import com.anrisoftware.sscontrol.hosts.service.internal.HostsImpl.HostsImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * Creates the hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class HostsServiceImpl implements HostsService {

    static final String HOSTS_NAME = "hosts";

    @Inject
    private HostsImplFactory hostsFactory;

    @Override
    public String getName() {
        return "hosts";
    }

    @Override
    public Hosts create(Map<String, Object> args) {
        return (Hosts) hostsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new HostsModule()).injectMembers(this);
    }
}
