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
package com.anrisoftware.sscontrol.command.shell.internal.copy;

import com.anrisoftware.sscontrol.command.copy.external.Copy;
import com.anrisoftware.sscontrol.command.copy.external.Copy.CopyFactory;
import com.anrisoftware.sscontrol.command.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.copy.ScpCopyWorker.ScpCopyWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CopyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Copy.class, CopyImpl.class)
                .build(CopyFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Copy.class, ScpCopyWorker.class)
                .build(ScpCopyWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Copy.class, DownloadCopyWorker.class)
                .build(DownloadCopyWorkerFactory.class));
    }

}
