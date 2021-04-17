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
package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultScriptInfoFactory;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappings;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappingsPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class SystemNameMappingsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(SystemInfo.class, DefaultSystemInfoImpl.class)
                .build(DefaultSystemInfoFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ScriptInfo.class, DefaultScriptInfoImpl.class)
                .build(DefaultScriptInfoFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SystemNameMappings.class,
                        SystemNameMappingsProperties.class)
                .build(SystemNameMappingsPropertiesFactory.class));
    }

}
