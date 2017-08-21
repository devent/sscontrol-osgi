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
package com.anrisoftware.sscontrol.etcd.service.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BindingImpl implements Binding {

    private URI address;

    @AssistedInject
    BindingImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    BindingImpl(@Assisted Map<String, Object> args) {
        try {
            parseArgs(args);
        } catch (URISyntaxException e) {
            throw new ParseBindingException(e, args);
        }
    }

    public void setAddress(String address) throws URISyntaxException {
        this.address = new URI(address);
    }

    public void setAddress(URI address) {
        this.address = address;
    }

    @Override
    public URI getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", getAddress())
                .toString();
    }

    private void parseArgs(Map<String, Object> args) throws URISyntaxException {
        Object v = args.get("address");
        if (v != null) {
            if (v instanceof URI) {
                this.address = (URI) v;
            } else {
                this.address = new URI(v.toString());
            }
        }
    }
}
