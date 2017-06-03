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
