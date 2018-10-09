package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateService;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.copy.external.CopyService;
import com.anrisoftware.sscontrol.command.fetch.external.FetchService;
import com.anrisoftware.sscontrol.command.replace.external.Replace;
import com.anrisoftware.sscontrol.command.replace.external.ReplaceService;
import com.anrisoftware.sscontrol.command.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Replace command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(ReplaceService.class)
public class ReplaceServiceImpl implements ReplaceService {

    @Inject
    private ReplaceFactory replaceFactory;

    @Reference
    private TokensTemplateService tokensTemplateService;

    @Reference
    private FetchService fetchService;

    @Reference
    private CopyService copyService;

    @Override
    public Replace create(Map<String, Object> args, SshHost host, Object parent,
            Threads threads, Object log) {
        return replaceFactory.create(args, host, parent, threads, log);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new ReplaceModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TokensTemplateService.class)
                        .toProvider(of(tokensTemplateService));
                bind(FetchService.class).toProvider(of(fetchService));
                bind(CopyService.class).toProvider(of(copyService));
            }
        }).injectMembers(this);
    }
}
