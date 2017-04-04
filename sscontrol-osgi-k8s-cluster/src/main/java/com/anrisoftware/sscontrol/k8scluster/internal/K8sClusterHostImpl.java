package com.anrisoftware.sscontrol.k8scluster.internal;

import java.net.UnknownHostException;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8scluster.external.Credentials;
import com.anrisoftware.sscontrol.k8scluster.external.CredentialsCert;
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterHost;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s</i> cluster host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sClusterHostImpl implements K8sClusterHost {

    private final SshHost target;

    private final Credentials credentials;

    /**
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sClusterHostImplFactory {

        K8sClusterHost create(SshHost target, Credentials credentials);
    }

    @Inject
    K8sClusterHostImpl(@Assisted SshHost target,
            @Assisted Credentials credentials) {
        this.target = target;
        this.credentials = credentials;
    }

    @Override
    public String getHost() {
        return credentials.getHost();
    }

    @Override
    public Integer getPort() {
        return credentials.getPort();
    }

    @Override
    public String getHostAddress() throws UnknownHostException {
        return target.getHostAddress();
    }

    @Override
    public String getType() {
        return credentials.getType();
    }

    @Override
    public String getName() {
        return credentials.getName();
    }

    @Override
    public Tls getTls() {
        if (credentials instanceof CredentialsCert) {
            CredentialsCert certs = (CredentialsCert) credentials;
            return certs.getTls();
        }
        return null;
    }

}
