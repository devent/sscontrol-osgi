package com.anrisoftware.sscontrol.types.external.credentials;

import java.net.URI;

/**
 * SSH credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface SshCredentials extends Credentials {

    URI getPublicKey();
}
