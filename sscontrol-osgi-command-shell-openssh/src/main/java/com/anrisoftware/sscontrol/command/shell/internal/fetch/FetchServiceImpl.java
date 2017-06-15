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
package com.anrisoftware.sscontrol.command.shell.internal.fetch;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch;
import com.anrisoftware.sscontrol.command.fetch.external.FetchService;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory;
import com.anrisoftware.sscontrol.command.shell.external.ScpService;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.AbstractModule;

/**
 * Fetch command service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(FetchService.class)
public class FetchServiceImpl implements FetchService {

    @Reference
    private ScpService scpService;

    @Inject
    private FetchFactory fetchFactory;

    @Override
    public Fetch create(Map<String, Object> args, SshHost ssh, Object parent,
            Threads threads, Object log) {
        return fetchFactory.create(args, ssh, parent, threads, log);
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