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
