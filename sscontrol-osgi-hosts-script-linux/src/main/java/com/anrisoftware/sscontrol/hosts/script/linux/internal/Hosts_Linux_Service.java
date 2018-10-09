package com.anrisoftware.sscontrol.hosts.script.linux.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.hosts.script.linux.external.Hosts_Linux_Factory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class Hosts_Linux_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "0";

    static final String SYSTEM_NAME = "linux";

    @Inject
    private Hosts_Linux_Factory hostsFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, TargetHost target, ExecutorService threads,
            Map<String, Object> env) {
        return hostsFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new Hosts_Linux_Module()).injectMembers(this);
    }

}
