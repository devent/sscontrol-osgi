package com.anrisoftware.sscontrol.k8scluster.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8scluster.external.Credentials
import com.google.inject.assistedinject.Assisted

/**
 * Empty credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CredentialsNop implements Credentials {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    static interface CredentialsNopFactory {

        Credentials create(Map args)
    }

    @Inject
    @Assisted
    private Map args

    @Override
    public String getType() {
        'anon'
    }

    void setProperty(String propertyName, Object newValue) {
        args[propertyName] = newValue
    }

    @Override
    public String getName() {
        args.name
    }

    @Override
    public String getHost() {
        args.host
    }

    @Override
    public Integer getPort() {
        args.port
    }
}
