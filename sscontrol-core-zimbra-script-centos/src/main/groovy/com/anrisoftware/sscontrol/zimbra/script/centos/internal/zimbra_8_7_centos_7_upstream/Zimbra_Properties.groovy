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
package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Zimbra 8.7 for CentOS 7 Upstream properties provider from
 * {@code "/zimbra_8_7_centos_7_upstream.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Zimbra_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Zimbra_Properties.class.getResource("/zimbra_8_7_centos_7_upstream.properties")

    Zimbra_Properties() {
        super(Zimbra_Properties.class, RESOURCE)
    }
}
