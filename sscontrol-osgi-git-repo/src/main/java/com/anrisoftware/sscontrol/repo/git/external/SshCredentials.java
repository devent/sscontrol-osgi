package com.anrisoftware.sscontrol.repo.git.external;

import java.net.URI;

/**
 * SSH credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface SshCredentials extends Credentials {

    URI getKey();
}
