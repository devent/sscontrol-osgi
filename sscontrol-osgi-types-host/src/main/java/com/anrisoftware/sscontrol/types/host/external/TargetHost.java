package com.anrisoftware.sscontrol.types.host.external;

import java.net.UnknownHostException;

/**
 * Target host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface TargetHost {

    String getProto();

    String getHost();

    Integer getPort();

    String getHostAddress() throws UnknownHostException;

}
