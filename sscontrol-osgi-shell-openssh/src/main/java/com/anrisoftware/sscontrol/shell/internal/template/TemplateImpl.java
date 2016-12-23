/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.template;

import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_HOST;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_KEY_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_PORT_ARG;
import static com.anrisoftware.sscontrol.shell.external.Cmd.SSH_USER_ARG;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle.Control;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;
import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.resources.templates.external.TemplateResource;
import com.anrisoftware.resources.templates.external.Templates;
import com.anrisoftware.resources.templates.external.TemplatesFactory;
import com.anrisoftware.sscontrol.shell.external.ssh.ShellExecException;
import com.anrisoftware.sscontrol.shell.external.template.WriteTemplateException;
import com.anrisoftware.sscontrol.shell.internal.scp.ScpRun.ScpRunFactory;
import com.anrisoftware.sscontrol.template.external.Template;
import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TemplateImpl implements Template {

    private final Map<String, Object> args;

    private final SshHost host;

    private final Object parent;

    private final Threads threads;

    private final Object log;

    private File tmpFile;

    @Inject
    private ScpRunFactory scpRunFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    @Inject
    TemplateImpl(@Assisted Map<String, Object> args, @Assisted SshHost host,
            @Assisted("parent") Object parent, @Assisted Threads threads,
            @Assisted("log") Object log) {
        this.args = new HashMap<String, Object>(args);
        this.host = host;
        this.parent = parent;
        this.threads = threads;
        this.log = log;
        setupArgs();
        checkArgs();
    }

    @Override
    public ProcessTask call() throws AppException {
        try {
            String text = getText();
            File file = getTmpFile();
            FileUtils.write(file, text, getCharset());
            Map<String, Object> a = new HashMap<String, Object>(args);
            a.put("src", file);
            a.put("log", log);
            return scpRunFactory.create(a, parent, threads).call();
        } catch (IOException e) {
            throw new WriteTemplateException(e, args);
        } catch (CommandExecException e) {
            throw new ShellExecException(e, "scp");
        }
    }

    private File getTmpFile() throws IOException {
        File file = tmpFile;
        if (file == null) {
            file = File.createTempFile("template", null);
        }
        return file;
    }

    private String getText() {
        TemplateResource res = getResourceFromArgs();
        if (res == null) {
            Templates t = getTemplates();
            res = getResourceFromTemplates(t);
        }
        res.invalidate();
        String text = res.getText(getArgs());
        return text;
    }

    private TemplateResource getResourceFromArgs() {
        Object v = args.get(RESOURCE_ARG);
        if (v instanceof TemplateResource) {
            return (TemplateResource) v;
        }
        return null;
    }

    private Charset getCharset() {
        return (Charset) args.get(CHARSET_ARG);
    }

    private Object[] getArgs() {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) args.get(ARGS_ARG);
        if (list == null) {
            list = new ArrayList<Object>();
        }
        String name = getStringArg(NAME_ARG);
        if (name != null) {
            list.add(0, name);
        }
        return list.toArray();
    }

    private TemplateResource getResourceFromTemplates(Templates t) {
        String resource = getResource();
        Locale locale = getLocale();
        TemplateResource res = null;
        if (locale != null) {
            res = t.getResource(resource, locale);
        } else {
            res = t.getResource(resource);
        }
        return res;
    }

    private Templates getTemplates() {
        Templates t = null;
        String baseName = getBaseName();
        Map<Serializable, Serializable> attributes = getAttributes();
        ClassLoader classLoader = getClassLoader();
        Control control = getControl();
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

    private Locale getLocale() {
        return (Locale) args.get(LOCALE_ARG);
    }

    private String getResource() {
        return getStringArg(RESOURCE_ARG);
    }

    private Control getControl() {
        return (Control) args.get(CONTROL_ARG);
    }

    private ClassLoader getClassLoader() {
        return (ClassLoader) args.get(CLASS_LOADER_ARG);
    }

    @SuppressWarnings("unchecked")
    private Map<Serializable, Serializable> getAttributes() {
        return (Map<Serializable, Serializable>) args.get(ATTRIBUTES_ARG);
    }

    private String getBaseName() {
        return getStringArg(BASE_ARG);
    }

    private String getStringArg(Object key) {
        Object v = args.get(key);
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }

    private void checkArgs() {
        isTrue(args.containsKey(DEST_ARG), "%s=null", DEST_ARG);
        notNull(args.get(DEST_ARG), "%s=null", DEST_ARG);
    }

    private void setupArgs() {
        args.put("remoteSrc", false);
        args.put("remoteDest", true);
        args.put(SSH_USER_ARG, host.getUser());
        args.put(SSH_HOST, host.getHost());
        args.put(SSH_PORT_ARG, host.getPort());
        args.put(SSH_KEY_ARG, host.getKey());
    }

}
