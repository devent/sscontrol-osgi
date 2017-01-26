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
package com.anrisoftware.sscontrol.shell.internal.cmd;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.shell.external.ssh.CmdArgs;
import com.anrisoftware.sscontrol.shell.external.ssh.CmdArgs.CmdArgsFactory;
import com.anrisoftware.sscontrol.shell.external.ssh.CmdArgsService;

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
