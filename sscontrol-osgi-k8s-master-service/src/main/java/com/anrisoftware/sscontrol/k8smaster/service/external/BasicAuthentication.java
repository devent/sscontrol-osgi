package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.net.URI;

/**
 * Static Token File.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface BasicAuthentication extends Authentication {

    URI getFile();

    String getTokens();

}
