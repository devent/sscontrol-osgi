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
package com.anrisoftware.sscontrol.command.shell.internal.st;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class StTemplate {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
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
