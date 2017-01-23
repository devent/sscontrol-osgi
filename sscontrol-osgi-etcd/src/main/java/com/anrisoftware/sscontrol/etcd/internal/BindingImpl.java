/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.etcd.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.external.Binding;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BindingImpl implements Binding {

    private String address;

    private String scheme;

    private int port;

    @AssistedInject
    BindingImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    BindingImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    public void setAddress(String insecure) {
        this.address = insecure;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scheme", getScheme())
                .append("address", getAddress()).append("port", getPort())
                .toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            this.address = v.toString();
        }
        v = args.get("scheme");
        if (v != null) {
            this.scheme = v.toString();
        }
        v = args.get("port");
        if (v != null) {
            this.port = ((Number) v).intValue();
        }
    }
}
