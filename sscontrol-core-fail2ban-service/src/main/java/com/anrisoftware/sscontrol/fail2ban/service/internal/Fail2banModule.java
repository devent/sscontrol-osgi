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
package com.anrisoftware.sscontrol.fail2ban.service.internal;

import com.anrisoftware.sscontrol.fail2ban.service.external.Banning;
import com.anrisoftware.sscontrol.fail2ban.service.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.service.internal.BanningImpl.BanningImplFactory;
import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImpl.Fail2banImplFactory;
import com.anrisoftware.sscontrol.fail2ban.service.internal.JailImpl.JailImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class Fail2banModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, Fail2banImpl.class)
                .build(Fail2banImplFactory.class));
        install(new FactoryModuleBuilder().implement(Jail.class, JailImpl.class)
                .build(JailImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Banning.class, BanningImpl.class)
                .build(BanningImplFactory.class));
    }

}
