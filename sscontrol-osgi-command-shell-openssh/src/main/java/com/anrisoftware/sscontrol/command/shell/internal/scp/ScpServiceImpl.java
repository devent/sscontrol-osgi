package com.anrisoftware.sscontrol.command.shell.internal.scp;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.Scp;
import com.anrisoftware.sscontrol.command.shell.external.ScpService;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgsService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Scp command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(ScpService.class)
public class ScpServiceImpl implements ScpService {

    @Inject
    private ScpFactory scpFactory;

    @Reference
    private CmdArgsService cmdArgsService;

    @Override
    public Scp create(Map<String, Object> args, SshHost ssh, Object parent,
            Threads threads, Object log) {
        return scpFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new ScpModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(CmdArgsService.class).toProvider(of(cmdArgsService));
            }
        }).injectMembers(this);
    }
}
