/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.command.shell.internal.scp;

import com.anrisoftware.sscontrol.command.shell.external.Scp;
import com.anrisoftware.sscontrol.command.shell.external.Scp.ScpFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.CopyWorker.CopyWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.FetchWorker.FetchWorkerFactory;
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ScpModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Scp.class, ScpImpl.class)
                .build(ScpFactory.class));
        install(new FactoryModuleBuilder().implement(ScpRun.class, ScpRun.class)
                .build(ScpRunFactory.class));
        install(new FactoryModuleBuilder()
                .implement(FetchWorker.class, FetchWorker.class)
                .build(FetchWorkerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CopyWorker.class, CopyWorker.class)
                .build(CopyWorkerFactory.class));
    }
}
