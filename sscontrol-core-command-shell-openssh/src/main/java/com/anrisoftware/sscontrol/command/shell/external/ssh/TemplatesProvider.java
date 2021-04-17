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
package com.anrisoftware.sscontrol.command.shell.external.ssh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.stringtemplate.v4.STGroup;

import com.anrisoftware.resources.st.external.SerializiableGroup;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class TemplatesProvider implements Provider<Templates> {

    private final Templates templates;

    @Inject
    TemplatesProvider(TemplatesBaseProvider baseProvider,
            TemplatesFactory templatesFactory) {
        Map<Serializable, Serializable> attr = new HashMap<Serializable, Serializable>();
        ArrayList<Serializable> parents = new ArrayList<Serializable>();
        parents.add(new SerializiableGroup((STGroup) baseProvider.get()
                .getResource("ssh_base").getTemplate()));
        attr.put("imports", parents);
        this.templates = templatesFactory.create("CmdTemplates", attr);
    }

    @Override
    public Templates get() {
        return templates;
    }

}
