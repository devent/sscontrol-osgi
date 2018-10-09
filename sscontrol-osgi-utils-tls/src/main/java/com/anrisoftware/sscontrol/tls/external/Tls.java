package com.anrisoftware.sscontrol.tls.external;

import java.net.URI;
import java.util.Map;

import com.google.inject.assistedinject.Assisted;

/**
 * TLS certificates.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Tls {

    /**
     * TLS certificates factory.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TlsFactory {

        Tls create(@Assisted Map<String, Object> args);

        Tls create();

    }

    URI getCa();

    URI getCert();

    URI getKey();

    String getCaName();

    String getCertName();

    String getKeyName();

    void setCaName(String caName);

    void setCertName(String certName);

    void setKeyName(String keyName);
}
