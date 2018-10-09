package com.anrisoftware.sscontrol.registry.docker.service.external;

/**
 * User credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface UserCredentials extends Credentials {

    String getUser();

    String getPassword();
}
