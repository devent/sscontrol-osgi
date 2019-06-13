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
package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.service.external.AlwaysAllowAuthorization;
import com.anrisoftware.sscontrol.k8smaster.service.external.AuthorizationFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Allows all requests.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AlwaysAllowAuthorizationImpl implements AlwaysAllowAuthorization {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface AlwaysAllowAuthorizationImplFactory
            extends AuthorizationFactory {

    }

    @Inject
    AlwaysAllowAuthorizationImpl(@Assisted Map<String, Object> args) {
    }

    @Override
    public String getMode() {
        return "allow";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
