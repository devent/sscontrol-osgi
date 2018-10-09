package com.anrisoftware.sscontrol.types.host.external;

import java.util.Set;

/**
 * Contains the host service properties.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostServiceProperties {

    void setProperty(String name, String value);

    void addProperty(String property);

    String getProperty(String name);

    String getProperty(String name, String defaultValue);

    Set<String> getPropertyNames();

    boolean haveProperty(String name);
}
