package com.anrisoftware.sscontrol.fail2ban.service.internal;

import static com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImplLogger.m.jailAdded;
import static com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImplLogger.m.setDefaultJail;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.fail2ban.service.external.Jail;

/**
 * Logging for {@link Fail2banImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class Fail2banImplLogger extends AbstractLogger {

    enum m {

        setDefaultJail("Sets default jail {} for {}"),

        jailAdded("Jail {} added to {}");

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
     * Sets the context of the logger to {@link Fail2banImpl}.
     */
    public Fail2banImplLogger() {
        super(Fail2banImpl.class);
    }

    void defaultJailSet(Fail2banImpl fail2ban, Jail jail) {
        debug(setDefaultJail, jail, fail2ban);
    }

    void jailAdded(Fail2banImpl fail2ban, Jail jail) {
        debug(jailAdded, jail, fail2ban);
    }

}
