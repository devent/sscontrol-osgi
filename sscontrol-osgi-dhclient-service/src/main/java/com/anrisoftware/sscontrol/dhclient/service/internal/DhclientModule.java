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
package com.anrisoftware.sscontrol.dhclient.service.internal;

import com.anrisoftware.sscontrol.dhclient.service.internal.DeclareStatementImpl.DeclareStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.DhclientImpl.DhclientImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.OptionDeclarationImpl.OptionDeclarationImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.OptionStatementImpl.OptionStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.PrependStatementImpl.PrependStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.RequestsStatementImpl.RequestsStatementImplFactory;
import com.anrisoftware.sscontrol.dhclient.service.internal.SendStatementImpl.SendStatementImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DhclientModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, DhclientImpl.class)
                .build(DhclientImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(OptionStatementImpl.class, OptionStatementImpl.class)
                .build(OptionStatementImplFactory.class));
        install(new FactoryModuleBuilder().implement(
                OptionDeclarationImpl.class, OptionDeclarationImpl.class)
                .build(OptionDeclarationImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SendStatementImpl.class, SendStatementImpl.class)
                .build(SendStatementImplFactory.class));
        install(new FactoryModuleBuilder().implement(
                RequestsStatementImpl.class, RequestsStatementImpl.class)
                .build(RequestsStatementImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(DeclareStatementImpl.class,
                        DeclareStatementImpl.class)
                .build(DeclareStatementImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(PrependStatementImpl.class,
                        PrependStatementImpl.class)
                .build(PrependStatementImplFactory.class));
    }

}
