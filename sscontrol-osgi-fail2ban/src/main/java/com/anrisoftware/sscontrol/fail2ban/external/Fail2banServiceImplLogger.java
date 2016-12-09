/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban;

import static com.anrisoftware.sscontrol.security.fail2ban.Fail2banServiceImplLogger._.jail_added_debug;
import static com.anrisoftware.sscontrol.security.fail2ban.Fail2banServiceImplLogger._.jail_added_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Fail2banServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Fail2banServiceImplLogger extends AbstractLogger {

    enum _ {

        jail_added_debug("Jail {} added for {}."),

        jail_added_info("Jail '{}' added for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates a logger for {@link Fail2banServiceImpl}.
     */
    public Fail2banServiceImplLogger() {
        super(Fail2banServiceImpl.class);
    }

    void jailAdded(Fail2banServiceImpl service, Jail jail) {
        if (isDebugEnabled()) {
            debug(jail_added_debug, jail, service);
        } else {
            info(jail_added_info, jail.getService(), service.getName());
        }
    }

}
