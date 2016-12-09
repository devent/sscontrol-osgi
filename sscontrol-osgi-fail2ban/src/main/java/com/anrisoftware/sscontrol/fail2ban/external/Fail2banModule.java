/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban;

import com.anrisoftware.globalpom.resources.ResourcesModule;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapModule;
import com.anrisoftware.sscontrol.core.listproperty.ListPropertyModule;
import com.anrisoftware.sscontrol.security.service.SecService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the <i>Fail2ban</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Fail2banModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new StatementsMapModule());
        install(new ListPropertyModule());
        install(new ResourcesModule());
        install(new FactoryModuleBuilder().implement(SecService.class,
                Fail2banServiceImpl.class).build(Fail2banServiceFactory.class));
        install(new FactoryModuleBuilder().implement(Jail.class, Jail.class)
                .build(JailFactory.class));
    }

}
