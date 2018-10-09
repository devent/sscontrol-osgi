package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.Cmd;
import com.anrisoftware.sscontrol.command.shell.external.Shell;
import com.anrisoftware.sscontrol.command.shell.external.ShellService;
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory;
import com.anrisoftware.sscontrol.command.shell.external.ssh.OpenSshShellService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.Assisted;

/**
 * Creates the shell.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service({ ShellService.class, OpenSshShellService.class })
public class ShellServiceImpl implements ShellService, OpenSshShellService {

    @Inject
    private ShellFactory shellFactory;

    @Reference
    private Cmd cmd;

    @Override
    public Shell create(Map<String, Object> args, @Assisted SshHost ssh,
            Object parent, Threads threads, Object log) {
        return shellFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new ShellCmdModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(Cmd.class).toProvider(of(cmd));
            }
        }).injectMembers(this);
    }
}
