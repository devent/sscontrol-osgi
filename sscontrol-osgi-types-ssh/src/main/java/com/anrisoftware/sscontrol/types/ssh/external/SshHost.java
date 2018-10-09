package com.anrisoftware.sscontrol.types.ssh.external;

import java.io.File;
import java.net.URI;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * <i>Ssh</i> host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface SshHost extends TargetHost {

    String getUser();

    /**
     * Returns the private SSH key.
     */
    URI getKey();

    /**
     * Returns the socket file of a control master for multiplexing.
     */
    File getSocket();

    SystemInfo getSystem();
}
