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

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.security.service.SecServiceFactory;
import com.anrisoftware.sscontrol.security.service.SecServiceFactoryFactory;
import com.anrisoftware.sscontrol.security.service.SecServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Fail2ban</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(SecServiceFactoryFactory.class)
public class Fail2banFactoryFactory implements SecServiceFactoryFactory {

    private static final String BACKEND_CLASS = "com.anrisoftware.sscontrol.security.fail2ban.Backend";

    private static final String TYPE_CLASS = "com.anrisoftware.sscontrol.security.fail2ban.Type";

    /**
     * <i>Fail2ban</i> service name.
     */
    public static final String SERVICE_NAME = "fail2ban";

    /**
     * <i>Fail2ban</i> service information.
     */
    @SuppressWarnings("serial")
    public static final SecServiceInfo INFO = new SecServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new Fail2banModule() };

    private Injector injector;

    public Fail2banFactoryFactory() {
    }

    @Override
    public SecServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(Fail2banServiceFactory.class);
    }

    @Override
    public SecServiceInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

    @Override
    public void configureCompiler(Object compiler) throws Exception {
        CompilerConfiguration c = (CompilerConfiguration) compiler;
        importClasses(c);
    }

    private void importClasses(CompilerConfiguration c) {
        ImportCustomizer customizer = new ImportCustomizer();
        customizer.addImports(TYPE_CLASS, BACKEND_CLASS);
        c.addCompilationCustomizers(customizer);
    }
}
