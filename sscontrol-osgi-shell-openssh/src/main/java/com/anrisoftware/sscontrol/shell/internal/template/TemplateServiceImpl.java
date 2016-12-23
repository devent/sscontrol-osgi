/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.template;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.shell.external.ScpService;
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule;
import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory;
import com.anrisoftware.sscontrol.template.external.TemplateService;
import com.anrisoftware.sscontrol.types.external.SshHost;
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
