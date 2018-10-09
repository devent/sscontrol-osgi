package com.anrisoftware.sscontrol.registry.docker.service.internal;

import java.util.Map;

import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials;

/**
 * Creates the access credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface CredentialsFactory {

    Credentials create(Map<String, Object> args);

}
