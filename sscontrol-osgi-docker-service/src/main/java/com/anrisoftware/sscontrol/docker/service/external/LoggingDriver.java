package com.anrisoftware.sscontrol.docker.service.external;

import java.util.Map;

/**
 * Defines the logging driver. See
 * <a href="https://docs.docker.com/config/containers/logging/">View logs for a
 * container or service.</a>
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface LoggingDriver {

    /**
     * Returns the name of the logging driver. For example:
     * <ul>
     * <li>json-file
     * <li>gelf
     * <li>syslog
     * <li>journald
     * <li>etc.
     * </ul>
     */
    String getDriver();

    /**
     * Returns the driver options.
     */
    Map<String, Object> getOpts();
}
