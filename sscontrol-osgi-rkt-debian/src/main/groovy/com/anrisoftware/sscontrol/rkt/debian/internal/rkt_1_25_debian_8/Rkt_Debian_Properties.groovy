/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.rkt.debian.internal.rkt_1_25_debian_8;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * rkt 1.25 for debian 8 properties provider from
 * {@code "/rkt_1_25_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Rkt_Debian_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Rkt_Debian_Properties.class.getResource("/rkt_1_25_debian_8.properties");

    Rkt_Debian_Properties() {
        super(Rkt_Debian_Properties.class, RESOURCE);
    }
}
