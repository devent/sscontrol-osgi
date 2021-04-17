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

import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * <i>Fail2ban</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Fail2ban extends HostService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     *
     * <pre>
     * service "fail2ban" with {
     *     debug "service", level: 1
     * }
     * </pre>
     */
    DebugLogging getDebugLogging();

    /**
     * Returns the default jail entry.
     * <p>
     *
     * <pre>
     * service "fail2ban", notify: "admin@${target.host.address}" with { }
     * // or
     * service "fail2ban" with {
     *     notify address: "admin@${target.host.address}"
     *     ignore address: "192.0.0.1" // or
     *     ignore << "192.0.0.1, 192.0.0.2"
     *     banning retries: 3, time: "PT10M", backend: Backend.polling, type: Type.deny
     * }
     * </pre>
     */
    Jail getDefaultJail();

    /**
     * Returns the jails.
     * <p>
     *
     * <pre>
     * service "fail2ban" with {
     *     jail "apache", notify: "root@localhost" with {
     *     }
     * }
     * </pre>
     */
    List<Jail> getJails();

}
