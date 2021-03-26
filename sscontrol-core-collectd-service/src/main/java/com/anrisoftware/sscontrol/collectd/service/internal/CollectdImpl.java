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
package com.anrisoftware.sscontrol.collectd.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.collectd.service.external.Collectd;
import com.anrisoftware.sscontrol.collectd.service.external.CollectdService;
import com.anrisoftware.sscontrol.collectd.service.external.Config;
import com.anrisoftware.sscontrol.collectd.service.internal.ConfigImpl.ConfigImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil.GeneticListProperty;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Collectd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CollectdImpl implements Collectd {

    public interface CollectdImplFactory extends CollectdService {

    }

    public static final String SERVICE_NAME = "collectd";

    private final CollectdImplLogger log;

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final List<Config> configs;

    private String version;

    @Inject
    private transient ConfigImplFactory configFactory;

    @AssistedInject
    CollectdImpl(CollectdImplLogger log,
            HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<TargetHost>();
        this.serviceProperties = propertiesService.create();
        this.configs = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * config << [name: "99-write_graphite", script: """..."""]
     * </pre>
     */
    public List<Map<String, Object>> getConfig() {
        return GeneticListPropertyUtil
                .<Map<String, Object>>geneticListStatement(
                        new GeneticListProperty<Map<String, Object>>() {

                            @Override
                            public void add(Map<String, Object> property) {
                                config(property);
                            }
                        });
    }

    /**
     * <pre>
     * config name: "99-write_graphite", script: """..."""
     * </pre>
     */
    public void config(Map<String, Object> args) {
        Config config = configFactory.create(args);
        configs.add(config);
        log.configAdded(this, config);
    }

    @Override
    public String getName() {
        return format("%s-%s", SERVICE_NAME, version);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public List<Config> getConfigs() {
        return configs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hosts", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseVersion(args);
        parseTargets(args);
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        this.version = v.toString();
    }

}
