package com.anrisoftware.sscontrol.sshd.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;

/**
 * Sshd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Sshd extends HostService {

    DebugLogging getDebugLogging();

    List<String> getUsers();

    /**
     * The binding of the sshd.
     */
    Binding getBinding();
}
