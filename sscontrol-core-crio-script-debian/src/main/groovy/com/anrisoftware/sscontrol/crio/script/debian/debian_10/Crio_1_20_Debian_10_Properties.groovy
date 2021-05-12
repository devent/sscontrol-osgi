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
package com.anrisoftware.sscontrol.crio.script.debian.debian_10

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * CRI-O 1.20 Debian 10 properties provider from {@code "/crio_1_20_debian_10.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Crio_1_20_Debian_10_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Crio_1_20_Debian_10_Properties.class.getResource("/crio_1_20_debian_10.properties")

    Crio_1_20_Debian_10_Properties() {
        super(Crio_1_20_Debian_10_Properties.class, RESOURCE)
    }
}
