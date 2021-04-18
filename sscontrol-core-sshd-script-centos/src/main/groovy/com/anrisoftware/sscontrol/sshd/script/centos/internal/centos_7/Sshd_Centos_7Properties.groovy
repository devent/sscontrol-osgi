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
package com.anrisoftware.sscontrol.sshd.script.centos.internal.centos_7

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Sshd Debian 10</i> properties provider from
 * {@code "/sshd_debian_10.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Sshd_Centos_7Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Sshd_Centos_7Properties.class.getResource("/sshd_debian_10.properties")

    Sshd_Centos_7Properties() {
        super(Sshd_Centos_7Properties.class, RESOURCE)
    }
}
