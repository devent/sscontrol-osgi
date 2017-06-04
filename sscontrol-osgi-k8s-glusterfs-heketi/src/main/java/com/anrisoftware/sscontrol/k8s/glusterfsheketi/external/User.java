package com.anrisoftware.sscontrol.k8s.glusterfsheketi.external;

/**
 * Secret for heketi general user. Heketi user has access to only Volume APIs.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface User {

    String getKey();
}
