package com.anrisoftware.sscontrol.etcd.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * X509 Peer Client Certs.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class PeerClientCertsAuthenticationImpl
        extends ClientCertsAuthenticationImpl {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface PeerClientCertsAuthenticationImplFactory
            extends AuthenticationFactory {

    }

    @Inject
    PeerClientCertsAuthenticationImpl(TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        super(tlsFactory, args);
    }

}
