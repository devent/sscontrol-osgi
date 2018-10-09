package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappings;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SystemNameMappingsProperties extends
        AbstractContextPropertiesProvider implements SystemNameMappings {

    private static final URL RESOURCE = SystemNameMappingsProperties.class
            .getResource("/system_name_mappings.properties");

    SystemNameMappingsProperties() {
        super(SystemNameMappingsProperties.class, RESOURCE);
    }

    @Override
    public String getMapping(String system) {
        String key = String.format("system_name_%s", system);
        return get().getProperty(key);
    }
}
