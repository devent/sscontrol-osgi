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
package com.anrisoftware.sscontrol.services.internal.registry;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.services.internal.targets.AbstractTargetsImpl;
import com.anrisoftware.sscontrol.types.registry.external.Registries;
import com.anrisoftware.sscontrol.types.registry.external.RegistriesService;
import com.anrisoftware.sscontrol.types.registry.external.Registry;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;

/**
 * Image registries targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RegistriesImpl extends AbstractTargetsImpl<RegistryHost, Registry>
        implements Registries {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface RegistriesImplFactory extends RegistriesService {

    }

    @Inject
    RegistriesImpl() {
    }
}
