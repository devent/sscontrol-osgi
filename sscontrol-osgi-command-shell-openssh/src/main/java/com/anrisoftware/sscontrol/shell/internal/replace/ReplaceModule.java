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

import com.anrisoftware.sscontrol.replace.external.Replace;
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.CreateTempFileWorker.CreateTempFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.LoadFileWorker.LoadFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.ParseSedSyntax.ParseSedSyntaxFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.PushFileWorker.PushFileWorkerFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceLine.ReplaceLineFactory;
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceWorker.ReplaceWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ReplaceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Replace.class, ReplaceImpl.class)
                .build(ReplaceFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ReplaceWorker.class, ReplaceWorker.class)
                .build(ReplaceWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(LoadFileWorker.class, LoadFileWorker.class)
                .build(LoadFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(PushFileWorker.class, PushFileWorker.class)
                .build(PushFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CreateTempFileWorker.class,
                        CreateTempFileWorker.class)
                .build(CreateTempFileWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ParseSedSyntax.class, ParseSedSyntax.class)
                .build(ParseSedSyntaxFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ReplaceLine.class, ReplaceLine.class)
                .build(ReplaceLineFactory.class));
    }

}
