package com.anrisoftware.sscontrol.docker.external;

import java.net.URI;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Registry mirror.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Mirror {

    URI getHost();

    Tls getTls();
}
