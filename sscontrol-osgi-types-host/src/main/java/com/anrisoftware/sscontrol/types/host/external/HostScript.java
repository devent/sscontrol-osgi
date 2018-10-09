package com.anrisoftware.sscontrol.types.host.external;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Configures the host system.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostScript extends HostService, Runnable {

    /**
     * Returns {@link String} name of the script.
     */
    String getName();

    /**
     * Returns the logger of the script.
     */
    Object getLog();

    /**
     * Returns {@link ExecutorService} pool to run the scripts on.
     */
    <T extends ExecutorService> T getThreads();

    /**
     * Returns the default {@link Properties} for the service.
     */
    Properties getDefaultProperties();

    /**
     * Returns the {@link HostServiceScript} scripts.
     */
    HostServices getScriptsRepository();
}
