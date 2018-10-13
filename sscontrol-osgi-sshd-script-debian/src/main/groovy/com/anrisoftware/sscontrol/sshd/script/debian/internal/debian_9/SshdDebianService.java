package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9;

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
public class SshdDebianService implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "8";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private SshdDebianFactory scriptFactory;

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
        createInjector(new SshdDebianModule()).injectMembers(this);
    }

}
