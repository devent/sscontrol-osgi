package com.anrisoftware.sscontrol.types.host.external;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Creates the host service script.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface HostServiceScriptService {

    /**
     * Creates the script.
     */
    HostServiceScript create(HostServices repository, HostService service,
            TargetHost target, ExecutorService threads,
            Map<String, Object> scriptEnv);
}
