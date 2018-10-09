package com.anrisoftware.sscontrol.command.shell.internal.template;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.shell.external.ScpService;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule;
import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.anrisoftware.sscontrol.template.external.TemplateService;
import com.google.inject.AbstractModule;

/**
 * Template command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(TemplateService.class)
public class TemplateServiceImpl implements TemplateService {

    @Reference
    private ScpService scpService;

    @Inject
    private TemplateFactory templateFactory;

    @Override
    public Template create(Map<String, Object> args, SshHost target,
            Object parent, Threads threads, Object log) {
        return templateFactory.create(args, target, parent, threads, log);
    }

    @Activate
    protected void start() {
        createInjector(new FetchModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ScpFactory.class).toProvider(of(scpService));
            }
        }).injectMembers(this);
    }
}
