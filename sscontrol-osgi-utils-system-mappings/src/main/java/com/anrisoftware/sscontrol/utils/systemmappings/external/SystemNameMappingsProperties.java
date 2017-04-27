package com.anrisoftware.sscontrol.utils.systemmappings.external;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Singleton
@SuppressWarnings("serial")
public class SystemNameMappingsProperties
        extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = SystemNameMappingsProperties.class
            .getResource("/system_name_mappings.properties");

    SystemNameMappingsProperties() {
        super(SystemNameMappingsProperties.class, RESOURCE);
    }

    public String getMapping(String system) {
        String key = String.format("system_name_%s", system);
        return get().getProperty(key);
    }
}
