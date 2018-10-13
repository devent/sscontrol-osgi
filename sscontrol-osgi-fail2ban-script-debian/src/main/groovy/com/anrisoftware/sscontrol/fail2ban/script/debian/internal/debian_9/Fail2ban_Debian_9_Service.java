package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9;

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
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component(service = HostServiceScriptService.class)
public class Fail2ban_Debian_9_Service implements HostServiceScriptService {

    static final String SYSTEM_VERSION = "8";

    static final String SYSTEM_NAME = "debian";

    @Inject
    private Fail2ban_Debian_9_Factory hostnameFactory;

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }

    @Override
    public HostServiceScript create(HostServices repository, HostService service, TargetHost target,
            ExecutorService threads, Map<String, Object> env) {
        return hostnameFactory.create(repository, service, target, threads, env);
    }

    @Activate
    protected void start() {
        createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                install(new FactoryModuleBuilder().implement(HostServiceScript.class, Fail2ban_Debian_9.class)
                        .build(Fail2ban_Debian_9_Factory.class));
            }
        }).injectMembers(this);
    }

}
