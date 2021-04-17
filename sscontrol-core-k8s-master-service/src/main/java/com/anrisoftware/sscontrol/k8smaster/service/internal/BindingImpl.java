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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.service.external.Binding;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BindingImpl implements Binding {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface BindingImplFactory {

        Binding create();

        Binding create(@Assisted Map<String, Object> args);

    }

    private String insecure;

    private String secure;

    private Integer port;

    private Integer insecurePort;

    @AssistedInject
    BindingImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    BindingImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setInsecureAddress(String insecure) {
        this.insecure = insecure;
    }

    @Override
    public String getInsecureAddress() {
        return insecure;
    }

    public void setSecureAddress(String secure) {
        this.secure = secure;
    }

    @Override
    public String getSecureAddress() {
        return secure;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setInsecurePort(int insecurePort) {
        this.insecurePort = insecurePort;
    }

    @Override
    public Integer getInsecurePort() {
        return insecurePort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("insecure");
        if (v != null) {
            this.insecure = v.toString();
        }
        v = args.get("secure");
        if (v != null) {
            this.secure = v.toString();
        }
        v = args.get("port");
        if (v != null) {
            this.port = ((Number) v).intValue();
        }
        v = args.get("insecurePort");
        if (v != null) {
            this.insecurePort = ((Number) v).intValue();
        }
    }
}
