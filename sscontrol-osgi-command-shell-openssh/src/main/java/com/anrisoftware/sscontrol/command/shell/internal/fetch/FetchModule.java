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

import com.anrisoftware.sscontrol.command.fetch.external.Fetch;
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FetchModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Fetch.class, FetchImpl.class)
                .build(FetchFactory.class));
        install(new FactoryModuleBuilder().implement(ScpRun.class, ScpRun.class)
                .build(ScpRunFactory.class));
    }

}
