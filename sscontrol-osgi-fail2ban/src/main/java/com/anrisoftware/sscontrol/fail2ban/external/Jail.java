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
package com.anrisoftware.sscontrol.fail2ban.external;

import java.util.List;

import org.joda.time.Duration;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Jail {

    /**
     * Returns the jail service name.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     * }
     * </pre>
     */
    String getService();

    /**
     * Returns the email address for notification.
     * <p>
     *
     * <pre>
     * jail "apache", notify: "root@localhost" with {
     * }
     * </pre>
     */
    String getNotify();

    /**
     * Returns the addresses to ignore.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     ignore address: "192.0.0.1" // or
     *     ignore << "192.0.0.1, 192.0.0.2"
     * }
     * </pre>
     */
    List<String> getIgnoreAddresses();

    /**
     * Returns the maximum retries.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning retries: 3
     * }
     * </pre>
     */
    Integer getBanningRetries();

    /**
     * Returns the banning time.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning time: "PT10M"
     * }
     * </pre>
     */
    Duration getBanningTime();

    /**
     * Returns the banning backend.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning backend: Backend.polling
     * }
     * </pre>
     */
    Backend getBanningBackend();

    /**
     * Returns the banning type.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning type: Type.deny
     * }
     * </pre>
     */
    Type getBanningType();

    /**
     * Returns the banning application name.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning app: "OpenSSH"
     * }
     * </pre>
     */
    String getBanningApp();
}
