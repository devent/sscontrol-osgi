package com.anrisoftware.sscontrol.repo.git.internal;

import java.util.Map;

import com.anrisoftware.sscontrol.repo.git.external.Credentials;

/**
 * Creates the access credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface CredentialsFactory {

    Credentials create(Map<String, Object> args);

}
