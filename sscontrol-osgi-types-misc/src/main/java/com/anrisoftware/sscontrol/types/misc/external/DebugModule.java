package com.anrisoftware.sscontrol.types.misc.external;

import java.util.Map;

/**
 * Debug logging module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DebugModule {

    /**
     * Returns a new module with the new name.
     */
    DebugModule rename(String name);

    /**
     * Returns the name of the module.
     */
    String getName();

    /**
     * Returns the logging level.
     */
    int getLevel();

    /**
     * Returns the properties.
     */
    Map<String, Object> getProperties();

    /**
     * Returns a new module with the put property value.
     */
    DebugModule putProperty(String property, Object value);

}
