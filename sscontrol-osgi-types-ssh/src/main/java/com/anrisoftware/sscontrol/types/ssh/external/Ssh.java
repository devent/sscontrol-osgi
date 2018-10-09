package com.anrisoftware.sscontrol.types.ssh.external;

import com.anrisoftware.sscontrol.types.host.external.TargetHostService;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;

/**
 * <i>Ssh</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Ssh extends TargetHostService<SshHost> {

    /**
     * Returns the debug logging.
     */
    DebugLogging getDebugLogging();

}
