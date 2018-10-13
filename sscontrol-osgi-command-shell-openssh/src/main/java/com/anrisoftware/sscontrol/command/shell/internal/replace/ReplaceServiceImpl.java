package com.anrisoftware.sscontrol.command.shell.internal.replace;

/*-
 * #%L
 * sscontrol-osgi - command-shell-openssh
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateService;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.copy.external.CopyService;
import com.anrisoftware.sscontrol.command.fetch.external.FetchService;
import com.anrisoftware.sscontrol.command.replace.external.Replace;
import com.anrisoftware.sscontrol.command.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.command.replace.external.ReplaceService;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Replace command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = ReplaceService.class)
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
    public Replace create(Map<String, Object> args, SshHost host, Object parent, Threads threads, Object log) {
        return replaceFactory.create(args, host, parent, threads, log);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new ReplaceModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TokensTemplateService.class).toProvider(of(tokensTemplateService));
                bind(FetchService.class).toProvider(of(fetchService));
                bind(CopyService.class).toProvider(of(copyService));
            }
        }).injectMembers(this);
    }
}
