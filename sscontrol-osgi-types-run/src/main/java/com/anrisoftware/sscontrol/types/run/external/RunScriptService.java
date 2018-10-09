package com.anrisoftware.sscontrol.types.run.external;

import java.util.concurrent.ExecutorService;

import com.anrisoftware.sscontrol.types.host.external.HostServices;

/**
 * Script runner service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface RunScriptService {

    RunScript create(ExecutorService threads, HostServices repository);

}
