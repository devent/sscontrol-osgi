/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.fail2ban.service.external;

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
     * Returns the jail port. The jail port can be set to a number if the port is
     * not the default port number.
     * <p>
     *
     * <pre>
     * jail "sshd", port: 22222 with {
     * }
     * </pre>
     */
    Integer getPort();

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
     * Returns the ban action. Only for the default jail section.
     */
    String getBanAction();

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
