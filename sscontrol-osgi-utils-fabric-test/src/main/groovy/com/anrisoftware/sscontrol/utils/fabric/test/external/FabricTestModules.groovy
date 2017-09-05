/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatModule
import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FabricTestModules {

    static Injector createInjector() {
        Guice.createInjector(
                new K8sModule(),
                new K8sClusterModule(),
                new DebugLoggingModule(),
                new TlsModule(),
                new SshModule(),
                new TypesModule(),
                new StringsModule(),
                new TokensTemplateModule(),
                new ResourcesModule(),
                new SystemNameMappingsModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new DurationSimpleFormatModule(),
                new DurationFormatModule(),
                new HostServicePropertiesServiceModule(),
                )
    }
}
