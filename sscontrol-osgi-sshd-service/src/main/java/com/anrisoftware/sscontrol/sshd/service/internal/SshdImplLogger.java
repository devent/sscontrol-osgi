package com.anrisoftware.sscontrol.sshd.service.internal;

import static com.anrisoftware.sscontrol.sshd.service.internal.SshdImplLogger.m.addUser;
import static com.anrisoftware.sscontrol.sshd.service.internal.SshdImplLogger.m.bindingSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.sshd.service.external.Binding;

/**
 * Logging for {@link SshdImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class SshdImplLogger extends AbstractLogger {

    enum m {

        addUser("User '{}' added to {}"),

        bindingSet("Binding {} set for {}");

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
     * Sets the context of the logger to {@link SshdImpl}.
     */
    public SshdImplLogger() {
        super(SshdImpl.class);
    }

    void addUser(SshdImpl sshd, String name) {
        debug(addUser, name, sshd);
    }

    void bindingSet(SshdImpl sshd, Binding binding) {
        debug(bindingSet, binding, sshd);
    }

}
