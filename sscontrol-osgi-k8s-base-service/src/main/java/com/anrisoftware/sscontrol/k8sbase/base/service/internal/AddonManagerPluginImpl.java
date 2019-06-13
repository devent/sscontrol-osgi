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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.AddonManagerPlugin;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Addon-Manager</i> plugin.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AddonManagerPluginImpl implements AddonManagerPlugin {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface AddonManagerPluginImplFactory extends PluginFactory {

    }

    @AssistedInject
    AddonManagerPluginImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    AddonManagerPluginImpl(@Assisted Map<String, Object> args) {
    }

    @Override
    public String getName() {
        return "addon-manager";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
