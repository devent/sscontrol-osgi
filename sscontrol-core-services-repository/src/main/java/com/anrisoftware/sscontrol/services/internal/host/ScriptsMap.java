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
package com.anrisoftware.sscontrol.services.internal.host;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractSystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsProperties;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ScriptsMap extends HashMap<ScriptInfo, HostServiceScriptService> {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ScriptsMapFactory {

        ScriptsMap create();

    }

    private final SystemNameMappingsProperties mappingsProperties;

    @Inject
    ScriptsMap(SystemNameMappingsProperties mappingsProperties) {
        this.mappingsProperties = mappingsProperties;
    }

    @Override
    public HostServiceScriptService get(Object key) {
        HostServiceScriptService s = super.get(key);
        ScriptInfo info = (ScriptInfo) key;
        String name = info.getName();
        String system = mappingsProperties.getMapping(name);
        if (name == null && system == null) {
            s = findScriptService(info.getService());
        }
        if (s == null) {
            s = super.get(createInfo(info, system, name, info.getVersion()));
        }
        if (s == null) {
            s = super.get(createInfo(info, system, name, "0"));
        }
        if (s == null) {
            s = super.get(createInfo(info, system, system, "0"));
        }
        return s;
    }

    private HostServiceScriptService findScriptService(String service) {
        for (Map.Entry<ScriptInfo, HostServiceScriptService> services : this
                .entrySet()) {
            if (services.getKey().getService().equals(service)) {
                return services.getValue();
            }
        }
        return null;
    }

    private AbstractScriptInfo createInfo(ScriptInfo info, String system,
            String name, String version) {
        return new AbstractScriptInfo(info.getService(),
                new AbstractSystemInfo(system, name, version) {
                }) {
        };
    }
}
