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
     * Returns if the jail is enabled.
     * <p>
     *
     * <pre>
     * jail "apache", enabled: true with {
     * }
     * </pre>
     */
    Boolean getEnabled();

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
     * Returns the jail banning arguments.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning retries: 3, ...
     * }
     * </pre>
     */
    Banning getBanning();
}
