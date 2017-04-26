package com.anrisoftware.sscontrol.types.external.ssh;

import java.net.UnknownHostException;

/**
 * Target host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface TargetHost {

    String getHost();

    Integer getPort();

    String getHostAddress() throws UnknownHostException;

}
