/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Fail2ban Debian 9 properties provider from
 * {@code "/fail2ban_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fail2ban_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Fail2ban_Debian_9_Properties.class.getResource("/fail2ban_debian_9.properties");

    Fail2ban_Debian_9_Properties() {
        super(Fail2ban_Debian_9_Properties.class, RESOURCE);
    }
}
