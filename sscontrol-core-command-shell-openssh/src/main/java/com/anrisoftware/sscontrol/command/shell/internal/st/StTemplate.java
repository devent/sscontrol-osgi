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
package com.anrisoftware.sscontrol.command.shell.internal.st;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class StTemplate {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface StTemplateFactory {

        StTemplate create(@Assisted Map<String, Object> args,
                @Assisted Object parent);
    }

    private final Map<String, Object> args;

    private final String template;

    @Inject
    StTemplate(@Assisted Map<String, Object> args, @Assisted Object parent) {
        this.args = getArgs(args, parent);
        this.template = args.get("st").toString();
    }

    private Map<String, Object> getArgs(Map<String, Object> args,
            Object parent) {
        Map<String, Object> a = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, Object> v = (Map<String, Object>) args.get("vars");
        a.put("vars", v);
        a.put("parent", parent);
        return a;
    }

    /**
     * Returns the text from the template.
     */
    public String getText() {
        ST st = new ST(template);
        st.add("vars", args.get("vars"));
        st.add("parent", args.get("parent"));
        return st.render();
    }

}
