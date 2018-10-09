package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class TemplatesBaseProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    TemplatesBaseProvider(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create("DockerRegistry_Linux_Templates");
    }

    @Override
    public Templates get() {
        return templates;
    }
}
