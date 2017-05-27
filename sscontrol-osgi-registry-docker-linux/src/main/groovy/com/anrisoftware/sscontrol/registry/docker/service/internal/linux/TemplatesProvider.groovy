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
package com.anrisoftware.sscontrol.registry.docker.service.internal.linux;

import javax.inject.Inject;
import javax.inject.Provider;

import org.stringtemplate.v4.STGroup;

import com.anrisoftware.resources.st.external.SerializiableGroup;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class TemplatesProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    TemplatesProvider(
    TemplatesBaseProvider baseProvider,
    TemplatesFactory templatesFactory) {
        def attr = new HashMap<Serializable, Serializable>()
        def parents = new ArrayList<Serializable>()
        parents << new SerializiableGroup((STGroup) baseProvider.get().getResource("docker_cmd_base").getTemplate())
        attr.imports = parents
        this.templates = templatesFactory.create("DockerRegistry_Linux_Templates", attr);
    }

    @Override
    public Templates get() {
        return templates;
    }
}
