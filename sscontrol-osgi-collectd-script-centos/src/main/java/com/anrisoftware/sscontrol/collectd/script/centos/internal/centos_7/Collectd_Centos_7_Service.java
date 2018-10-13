package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7;

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

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
@Component(service = HostServiceScriptService.class)
public class Collectd_Centos_7_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "7";

    static final String SYSTEM_NAME = "centos";

    @Inject
    private Collectd_Centos_7_Factory scriptFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices rep, HostService service, TargetHost target, ExecutorService threads,
            Map<String, Object> env) {
        return scriptFactory.create(rep, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new Collectd_Centos_7_Module()).injectMembers(this);
    }

}
