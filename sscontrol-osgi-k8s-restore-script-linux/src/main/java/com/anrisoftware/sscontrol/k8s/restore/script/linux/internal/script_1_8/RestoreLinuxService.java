package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8;

import static com.google.inject.Guice.createInjector;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractSystemInfo;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(HostServiceScriptService.class)
public class RestoreLinuxService implements HostServiceScriptService {

    static final String SERVICE_NAME = "restore";

    @Property(value = SERVICE_NAME)
    static final String SERVICE_NAME_PROPERTY = "service.name";

    static final String SYSTEM_VERSION = "0";

    @Property(value = SYSTEM_VERSION)
    static final String SERVICE_SYSTEM_VERSION_PROPERTY = "service.system.version";

    static final String SYSTEM_NAME = "linux";

    @Property(value = SYSTEM_NAME)
    static final String SERVICE_SYSTEM_NAME_PROPERTY = "service.system.name";

    static final String SYSTEM_SYSTEM = "linux";

    @Property(value = SYSTEM_SYSTEM)
    static final String SERVICE_SYSTEM_SYSTEM_PROPERTY = "service.system.system";

    @Inject
    private RestoreLinuxFactory scriptFactory;

    public SystemInfo getSystem() {
        return new AbstractScriptInfo(SERVICE_NAME, new AbstractSystemInfo(
                SYSTEM_SYSTEM, SYSTEM_NAME, SYSTEM_VERSION) {
        }) {
        };
    }

    @Override
    public HostServiceScript create(HostServices repository,
            HostService service, TargetHost target, ExecutorService threads,
            Map<String, Object> env) {
        return scriptFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new RestoreLinuxModule()).injectMembers(this);
    }

}
