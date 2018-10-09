package com.anrisoftware.sscontrol.ssh.service.internal;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.core.strings.ToStringService;
import com.anrisoftware.sscontrol.ssh.service.external.SshService;
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * <i>Ssh</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class SshServiceImpl implements SshService {

    @Inject
    private SshImplFactory sshFactory;

    @Reference
    private ToStringService toStringService;

    @Override
    public String getName() {
        return "ssh";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return sshFactory.create(args);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new SshModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }
}
