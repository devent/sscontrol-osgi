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

import org.joda.time.Duration;

/**
 * The jail banning arguments.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public interface Banning {

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
    Integer getRetries();

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
    Duration getTime();

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
    Backend getBackend();

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
    Type getType();

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
    String getApp();

    /**
     * Returns the banning action.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning action: "%(banaction)s[name=%(__name__)s, port="%(port)s", protocol="%(protocol)s", chain="%(chain)s"]"
     * }
     * </pre>
     */
    String getAction();
}
