package com.anrisoftware.sscontrol.k8scluster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8scluster.service.external.CredentialsCertFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.cluster.external.CredentialsCert;
import com.google.inject.assistedinject.Assisted;

/**
 * Certificate based credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CredentialsCertImpl extends AbstractCredentials
        implements CredentialsCert {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CredentialsCertImplFactory extends CredentialsCertFactory {

    }

    private Tls tls;

    @Inject
    CredentialsCertImpl(AbstractCredentialsLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        super(log, args);
        this.tls = tlsFactory.create(args);
        parseArgs(args, tlsFactory);
    }

    @Override
    public String getType() {
        return "cert";
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    private void parseArgs(Map<String, Object> args, TlsFactory tlsFactory) {
        Object ca = args.get("ca");
        Object cert = args.get("cert");
        Object key = args.get("key");
        if (ca != null || cert != null || key != null) {
            this.tls = tlsFactory.create(args);
        }
    }

}
