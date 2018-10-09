package com.anrisoftware.sscontrol.types.host.external;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Host service script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostServiceScript {

    /**
     * Executes the script.
     */
    Object run();

    /**
     * Returns the system name, for example {@code "ubuntu"}.
     */
    String getSystemName();

    /**
     * Returns the system version, for example {@code "14.04"}.
     */
    String getSystemVersion();

    /**
     * Returns the logger of the script.
     */
    Object getLog();

    /**
     * Returns service {@link HostServiceProperties} properties.
     */
    HostServiceProperties getProperties();

    /**
     * Returns {@link ExecutorService} pool to run the scripts on.
     */
    <T extends ExecutorService> T getThreads();

    /**
     * Returns the default {@link Properties} for the service.
     */
    Properties getDefaultProperties();

    /**
     * Returns the {@link HostService} script.
     */
    HostService getService();

    /**
     * Returns the {@link HostServiceScript} scripts.
     */
    HostServices getScriptsRepository();

    /**
     * Returns the {@link TargetHost} target.
     */
    TargetHost getTarget();
}
