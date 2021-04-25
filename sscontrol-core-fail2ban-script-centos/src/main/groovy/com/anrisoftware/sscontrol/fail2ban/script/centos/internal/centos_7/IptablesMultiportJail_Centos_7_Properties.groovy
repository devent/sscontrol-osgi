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
package com.anrisoftware.sscontrol.fail2ban.script.centos.internal.centos_7;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Jail properties provider from
 * {@code "/iptables_multiport_jail_centos_7.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class IptablesMultiportJail_Centos_7_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = IptablesMultiportJail_Centos_7_Properties.class.getResource("/iptables_multiport_jail_centos_7.properties");

    IptablesMultiportJail_Centos_7_Properties() {
        super(IptablesMultiportJail_Centos_7_Properties.class, RESOURCE);
    }
}
