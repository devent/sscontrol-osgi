package com.anrisoftware.sscontrol.k8scluster.internal;

import static com.anrisoftware.sscontrol.k8scluster.internal.CredentialsCertImplLogger.m.nameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link CredentialsCertImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class CredentialsCertImplLogger extends AbstractLogger {

    enum m {

        nameSet("Name {} set for {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link CredentialsCertImpl}.
     */
    public CredentialsCertImplLogger() {
        super(CredentialsCertImpl.class);
    }

    void nameSet(CredentialsCertImpl cluster, String name) {
        debug(nameSet, name, cluster);
    }
}
