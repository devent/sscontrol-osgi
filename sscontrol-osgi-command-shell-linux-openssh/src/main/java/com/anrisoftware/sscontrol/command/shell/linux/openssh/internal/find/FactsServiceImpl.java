package com.anrisoftware.sscontrol.command.shell.linux.openssh.internal.find;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFiles;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesFactory;
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Find files command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(FindFilesService.class)
public class FactsServiceImpl implements FindFilesService {

    @Inject
    private FindFilesFactory filesFactory;

    @Override
    public FindFiles create(Map<String, Object> args, SshHost ssh,
            Object parent, Threads threads, Object log) {
        return filesFactory.create(args, ssh, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new FindModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
