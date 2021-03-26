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
package com.anrisoftware.sscontrol.inline.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.inline.external.Inline;
import com.anrisoftware.sscontrol.inline.external.InlineService;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Inline</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class InlineImpl implements Inline {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface InlineImplFactory extends InlineService {

    }

    private final InlineImplLogger log;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> targets;

    private final List<String> scripts;

    @AssistedInject
    InlineImpl(InlineImplLogger log, HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.scripts = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * Service property.
     * 
     * <pre>
     * // Add property.
     * property << "name=value"
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
     * Script.
     * 
     * <pre>
     * // Add script.
     * script << "name=value"
     * </pre>
     */
    public List<String> getScript() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String str) {
                scripts.add(str);
                log.scriptAdded(InlineImpl.this, str);
            }
        });
    }

    @Override
    public String getName() {
        return InlineServiceImpl.SERVICE_NAME;
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
        return targets;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public List<String> getScripts() {
        return scripts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).append("scripts", scripts.size())
                .toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
    }

}
