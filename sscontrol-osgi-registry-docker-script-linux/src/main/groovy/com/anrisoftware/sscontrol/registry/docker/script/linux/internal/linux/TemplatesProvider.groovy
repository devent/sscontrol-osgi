package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux;

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
