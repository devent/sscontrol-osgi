package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8;

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
import com.google.j2objc.annotations.Property;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component(service = HostServiceScriptService.class)
public class K8sClusterLinuxService implements HostServiceScriptService {

    static final String SERVICE_NAME = "hostname";

    @Property(value = SERVICE_NAME)
    static final String SERVICE_NAME_PROPERTY = "service.name";

    static final String SYSTEM_VERSION = "0";

    @Property(value = SYSTEM_VERSION)
    static final String SERVICE_SYSTEM_VERSION_PROPERTY = "service.system.version";

    static final String SYSTEM_NAME = "linux";

    @Property(value = SYSTEM_NAME)
    static final String SERVICE_SYSTEM_NAME_PROPERTY = "service.system.name";

    @Inject
    private K8sClusterLinuxFactory scriptFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository, HostService service, TargetHost target,
            ExecutorService threads, Map<String, Object> env) {
        return scriptFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new K8sClusterLinuxModule()).injectMembers(this);
    }

}
