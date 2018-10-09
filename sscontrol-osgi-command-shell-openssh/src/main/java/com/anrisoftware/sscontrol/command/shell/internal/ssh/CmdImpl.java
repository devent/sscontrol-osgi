package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.exec.external.runcommands.RunCommandsFactory;
import com.anrisoftware.globalpom.exec.external.runcommands.RunCommandsService;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.exec.external.scriptprocess.ScriptExecService;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.propertiesutils.ContextPropertiesService;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.resources.templates.external.TemplatesService;
import com.anrisoftware.sscontrol.command.shell.external.Cmd;
import com.google.inject.AbstractModule;

/**
 * Runs the specified command.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Component
@Service(Cmd.class)
public class CmdImpl implements Cmd {

    @Inject
    private CmdRunCaller cmdRunCaller;

    @Reference
    private ScriptExecService scriptEx;

    @Reference
    private RunCommandsService runCommandsService;

    @Reference
    private TemplatesService templatesService;

    @Reference
    private ContextPropertiesService contextProperties;

    @Override
    public ProcessTask call(Map<String, Object> args, Object parent,
            Threads threads, String command) throws CommandExecException {
        return cmdRunCaller.call(args, parent, threads, command);
    }

    @Activate
    protected void start() {
        createInjector(new SshShellModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(RunCommandsFactory.class)
                        .toProvider(of(runCommandsService));
                bind(TemplatesFactory.class).toProvider(of(templatesService));
                bind(ScriptExecFactory.class).toProvider(of(scriptEx));
            }
        }).injectMembers(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
