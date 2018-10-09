package com.anrisoftware.sscontrol.types.cluster.external;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Certificate based credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface CredentialsCert extends Credentials {

    Tls getTls();
}
