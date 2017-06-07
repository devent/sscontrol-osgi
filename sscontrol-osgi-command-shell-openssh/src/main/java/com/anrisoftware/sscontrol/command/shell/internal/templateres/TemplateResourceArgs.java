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
package com.anrisoftware.sscontrol.command.shell.internal.templateres;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle.Control;

import javax.inject.Inject;

import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns the template resource from the specified arguments.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TemplateResourceArgs {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TemplateResourceArgsFactory {

        TemplateResourceArgs create(@Assisted Map<String, Object> args,
                @Assisted Object parent);
    }

    private final String text;

    @Inject
    TemplateResourceArgs(@Assisted Map<String, Object> args,
            @Assisted Object parent, TemplatesFactory templatesFactory) {
        TemplateResource res = getResourceFromArgs(args);
        if (res == null) {
            Templates templates = getTemplates(templatesFactory, args, parent);
            res = getResourceFromTemplates(templates, args);
        }
        this.text = getText(args, parent, res);
    }

    /**
     * Returns the text from the template resource.
     */
    public String getText() {
        return text;
    }

    static final String BASE_ARG = "base";

    static final String ATTRIBUTES_ARG = "attributes";

    static final String CLASS_LOADER_ARG = "classLoader";

    static final String CONTROL_ARG = "control";

    static final String RESOURCE_ARG = "resource";

    static final String LOCALE_ARG = "locale";

    static final String VARS_ARG = "vars";

    static final String NAME_ARG = "name";

    private String getText(Map<String, Object> args, Object parent,
            TemplateResource res) {
        res.invalidate();
        String text = res.getText(getArgs(args, parent));
        return text;
    }

    private Templates getTemplates(TemplatesFactory templatesFactory,
            Map<String, Object> args, Object parent) {
        Templates t = null;
        String baseName = getBaseName(args);
        Map<Serializable, Serializable> attributes = getAttributes(args);
        ClassLoader classLoader = getClassLoader(args);
        Control control = getControl(args);
        if (baseName != null && attributes != null && classLoader != null
                && control != null) {
            t = templatesFactory.create(baseName, attributes, classLoader,
                    control);
        } else if (baseName != null && attributes != null && classLoader == null
                && control != null) {
            t = templatesFactory.create(baseName, attributes, control);
        } else if (baseName != null && attributes != null && classLoader != null
                && control == null) {
            t = templatesFactory.create(baseName, attributes, classLoader);
        } else if (baseName != null && attributes != null && classLoader == null
                && control == null) {
            t = templatesFactory.create(baseName, attributes);
        }
        // TemplatesFactory#create(String baseName)
        else if (baseName != null && attributes == null && classLoader == null
                && control == null) {
            t = templatesFactory.create(baseName);
        } else {
            notNull(baseName, "%s=null", BASE_ARG);
        }
        return t;
    }

    private TemplateResource getResourceFromArgs(Map<String, Object> args) {
        Object v = args.get(RESOURCE_ARG);
        if (v instanceof TemplateResource) {
            return (TemplateResource) v;
        }
        return null;
    }

    private Object[] getArgs(Map<String, Object> args, Object parent) {
        List<Object> list = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, Object> vars = (Map<String, Object>) args.get(VARS_ARG);
        if (vars == null) {
            vars = new HashMap<>();
        }
        String name = getStringArg(args, NAME_ARG);
        if (name != null) {
            list.add(0, name);
        }
        list.add("parent");
        list.add(parent);
        list.add("vars");
        list.add(vars);
        return list.toArray();
    }

    private TemplateResource getResourceFromTemplates(Templates t,
            Map<String, Object> args) {
        String resource = getResource(args);
        Locale locale = getLocale(args);
        TemplateResource res = null;
        if (locale != null) {
            res = t.getResource(resource, locale);
        } else {
            res = t.getResource(resource);
        }
        return res;
    }

    private Locale getLocale(Map<String, Object> args) {
        return (Locale) args.get(LOCALE_ARG);
    }

    private String getResource(Map<String, Object> args) {
        return getStringArg(args, RESOURCE_ARG);
    }

    private Control getControl(Map<String, Object> args) {
        return (Control) args.get(CONTROL_ARG);
    }

    private ClassLoader getClassLoader(Map<String, Object> args) {
        return (ClassLoader) args.get(CLASS_LOADER_ARG);
    }

    @SuppressWarnings("unchecked")
    private Map<Serializable, Serializable> getAttributes(
            Map<String, Object> args) {
        return (Map<Serializable, Serializable>) args.get(ATTRIBUTES_ARG);
    }

    private String getBaseName(Map<String, Object> args) {
        return getStringArg(args, BASE_ARG);
    }

    private String getStringArg(Map<String, Object> args, Object key) {
        Object v = args.get(key);
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }

}
