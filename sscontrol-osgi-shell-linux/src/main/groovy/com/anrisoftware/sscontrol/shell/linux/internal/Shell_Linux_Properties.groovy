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
package com.anrisoftware.sscontrol.shell.linux.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Shell GNU/Linux properties provider from
 * {@code "/shell_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Shell_Linux_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Shell_Linux_Properties.class.getResource("/shell_linux_0.properties");

    Shell_Linux_Properties() {
        super(Shell_Linux_Properties.class, RESOURCE);
    }
}
