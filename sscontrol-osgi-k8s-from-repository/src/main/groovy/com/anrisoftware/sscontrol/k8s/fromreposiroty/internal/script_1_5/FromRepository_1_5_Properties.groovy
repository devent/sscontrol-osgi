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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * From repository service for Kubernetes 1.5 properties provider from
 * {@code "/from_repository_1_5_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FromRepository_1_5_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = FromRepository_1_5_Properties.class.getResource("/from_repository_1_5_linux.properties")

    FromRepository_1_5_Properties() {
        super(FromRepository_1_5_Properties.class, RESOURCE)
    }
}
