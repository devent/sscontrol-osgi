/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.replace;

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateService;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.copy.external.CopyService;
import com.anrisoftware.sscontrol.fetch.external.FetchService;
import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
import com.anrisoftware.sscontrol.replace.external.ReplaceService;
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
