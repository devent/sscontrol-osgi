package com.anrisoftware.sscontrol.command.shell.internal.cmd;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgsService;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs.CmdArgsFactory;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Component
@Service(CmdArgsService.class)
public class CmdArgsServiceImpl implements CmdArgsService {

    @Inject
    private CmdArgsFactory cmdArgsFactory;

    @Override
    public CmdArgs create(Map<String, Object> args) {
        return cmdArgsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new CmdModule()).injectMembers(this);
    }

}
