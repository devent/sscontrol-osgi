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
package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Network;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Flannel-Docker</i> Etcd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class NetworkImpl implements Network {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface NetworkImplFactory {

        Network create();

        Network create(Map<String, Object> args);

    }

    private String address;

    @AssistedInject
    NetworkImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    NetworkImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", getAddress())
                .toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            setAddress(v.toString());
        }
    }

}
