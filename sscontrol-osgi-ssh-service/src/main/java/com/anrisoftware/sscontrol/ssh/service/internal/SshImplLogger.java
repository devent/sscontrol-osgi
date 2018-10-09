package com.anrisoftware.sscontrol.ssh.service.internal;

import static com.anrisoftware.sscontrol.ssh.service.internal.SshImplLogger.m.groupSet;
import static com.anrisoftware.sscontrol.ssh.service.internal.SshImplLogger.m.hostAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;

/**
 * Logging for {@link SshImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class SshImplLogger extends AbstractLogger {

    enum m {

        hostAdded("Host added {} to {}"),

        groupSet("Group '{}' set for {}");

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
     * Sets the context of the logger to {@link SshImpl}.
     */
    public SshImplLogger() {
        super(SshImpl.class);
    }

    void hostAdded(SshImpl ssh, SshHost host) {
        debug(hostAdded, host, ssh);
    }

    void groupSet(SshImpl ssh, String group) {
        debug(groupSet, group, ssh);
    }
}
