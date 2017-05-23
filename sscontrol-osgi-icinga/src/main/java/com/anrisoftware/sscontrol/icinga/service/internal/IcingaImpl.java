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
package com.anrisoftware.sscontrol.icinga.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.icinga.service.external.Icinga;
import com.anrisoftware.sscontrol.icinga.service.external.IcingaService;
import com.anrisoftware.sscontrol.icinga.service.external.Plugin;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * Icinga service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class IcingaImpl implements Icinga {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface IcingaImplFactory extends IcingaService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final IcingaImplLogger log;

    private final List<Plugin> plugins;

    private final List<String> configs;

    private String version;

    @Inject
    private transient Map<String, PluginFactory> pluginFactories;

    @Inject
    IcingaImpl(IcingaImplLogger log, HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.plugins = new ArrayList<>();
        this.configs = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * property << 'name=value'
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
     * plugin << "ido-mysql"
     * </pre>
     */
    public List<String> getPlugin() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                plugin(property);
            }
        });
    }

    /**
     * <pre>
     * plugin 'ido-mysql'
     * </pre>
     */
    public Plugin plugin(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        return plugin(args);
    }

    /**
     * <pre>
     * plugin name: 'ido-mysql'
     * </pre>
     */
    public Plugin plugin(Map<String, Object> args) {
        Object v = args.get("name");
        String name = v.toString();
        Plugin plugin = pluginFactories.get(name).create(args);
        log.pluginAdded(this, plugin);
        plugins.add(plugin);
        return plugin;
    }

    /**
     * <pre>
     * config << "icinga-config"
     * </pre>
     */
    public List<String> getConfig() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                config(property);
            }
        });
    }

    /**
     * <pre>
     * config 'icinga-config'
     * </pre>
     */
    public void config(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("text", name);
        config(args);
    }

    /**
     * <pre>
     * config text: 'ido-mysql'
     * </pre>
     */
    public void config(Map<String, Object> args) {
        Object v = args.get("text");
        String text = v.toString();
        configs.add(text);
        log.configAdded(this, text);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return format("icinga-%s", version);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public List<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public List<String> getConfigs() {
        return configs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseVersion(args);
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        this.version = v.toString();
    }

}
