package com.anrisoftware.sscontrol.docker.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;

/**
 * <i>Docker</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Docker extends HostService {

    List<String> getCgroups();

    Registry getRegistry();

    DebugLogging getDebugLogging();

    LoggingDriver getLoggingDriver();

}
