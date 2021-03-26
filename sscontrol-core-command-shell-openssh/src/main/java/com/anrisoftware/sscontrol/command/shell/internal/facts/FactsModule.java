/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.command.shell.internal.facts;

import com.anrisoftware.sscontrol.command.facts.external.Facts;
import com.anrisoftware.sscontrol.command.facts.external.Facts.FactsFactory;
import com.anrisoftware.sscontrol.command.shell.internal.facts.CatReleaseParse.CatReleaseParseFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FactsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Facts.class, FactsImpl.class)
                .build(FactsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(CatReleaseParse.class, CatReleaseParse.class)
                .build(CatReleaseParseFactory.class));
    }

}
