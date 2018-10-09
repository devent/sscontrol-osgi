package com.anrisoftware.sscontrol.k8scluster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8scluster.service.external.CredentialsCertFactory;
import com.anrisoftware.sscontrol.types.cluster.external.CredentialsAnon;
import com.google.inject.assistedinject.Assisted;

/**
 * Anonymous.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CredentialsAnonImpl extends AbstractCredentials
        implements CredentialsAnon {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CredentialsAnonImplFactory extends CredentialsCertFactory {

    }

    @Inject
    CredentialsAnonImpl(AbstractCredentialsLogger log,
            @Assisted Map<String, Object> args) {
        super(log, args);
    }

    @Override
    public String getType() {
        return "anon";
    }

}
