/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.shell.internal;

import static com.anrisoftware.sscontrol.shell.internal.ShellServiceImpl.SERVICE_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.shell.external.Script;
import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.ShellService;
import com.anrisoftware.sscontrol.shell.internal.ScriptImpl.ScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Shell service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ShellImpl implements Shell {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ShellImplFactory extends ShellService {

    }

    private final ShellImplLogger log;

    private final List<Script> scripts;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private transient ScriptImplFactory scriptFactory;

    private transient Map<String, Object> vars;

    @AssistedInject
    ShellImpl(ShellImplLogger log, HostPropertiesService propertiesService,
            ScriptImplFactory scriptFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.scripts = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.scriptFactory = scriptFactory;
        this.vars = new HashMap<>(args);
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    /**
     * <pre>
     * property << "property=value"
     * </pre>
     */
    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    /**
     * <pre>
     * script << "echo hello"
     * </pre>
     */
    public List<String> getScript() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                script(property);
            }

        });
    }

    /**
     * <pre>
     * script "echo hello"
     * </pre>
     */
    public void script(String command) {
        Map<String, Object> args = new HashMap<>(vars);
        args.put("command", command);
        Script script = scriptFactory.create(args);
        addScript(script);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
    }

    public void addScript(Script script) {
        scripts.add(script);
        log.scriptAdded(this, script);
    }

    @Override
    public List<Script> getScripts() {
        return scripts;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hosts", scripts).append("targets", targets).toString();
    }

}
