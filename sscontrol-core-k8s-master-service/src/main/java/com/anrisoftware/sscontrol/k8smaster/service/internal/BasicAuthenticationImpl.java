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
package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURIFactory;
import com.anrisoftware.sscontrol.k8smaster.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.k8smaster.service.external.BasicAuthentication;
import com.google.inject.assistedinject.Assisted;

/**
 * Static Token File.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class BasicAuthenticationImpl implements BasicAuthentication {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface BasicAuthenticationImplFactory
            extends AuthenticationFactory {

    }

    private URI file;

    private String tokens;

    @Inject
    BasicAuthenticationImpl(ToURIFactory touri,
            @Assisted Map<String, Object> args) {
        Object v = args.get("file");
        if (v != null) {
            this.file = touri.create(v).convert();
        } else {
            this.file = null;
        }
        v = args.get("tokens");
        if (v != null) {
            this.tokens = v.toString();
        } else {
            this.tokens = null;
        }
    }

    @Override
    public String getType() {
        return "basic";
    }

    @Override
    public URI getFile() {
        return file;
    }

    @Override
    public String getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
