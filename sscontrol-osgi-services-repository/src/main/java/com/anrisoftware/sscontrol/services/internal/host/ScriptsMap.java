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
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ScriptsMap extends HashMap<ScriptInfo, HostServiceScriptService> {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
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
