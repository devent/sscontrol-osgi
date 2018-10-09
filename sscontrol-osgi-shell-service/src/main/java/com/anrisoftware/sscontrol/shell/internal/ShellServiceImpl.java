package com.anrisoftware.sscontrol.shell.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.ShellService;
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * Creates the shell service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class ShellServiceImpl implements ShellService {

    static final String SERVICE_NAME = "shell";

    @Inject
    private ShellImplFactory serviceFactory;

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public Shell create(Map<String, Object> args) {
        return (Shell) serviceFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new ShellModule()).injectMembers(this);
    }
}
