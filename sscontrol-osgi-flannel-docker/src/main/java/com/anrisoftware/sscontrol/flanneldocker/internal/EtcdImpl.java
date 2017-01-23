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
package com.anrisoftware.sscontrol.flanneldocker.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.external.Etcd;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Flannel-Docker</i> Etcd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class EtcdImpl implements Etcd {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface EtcdImplFactory {

        Etcd create();

        Etcd create(Map<String, Object> args);

    }

    private String address;

    private String prefix;

    @AssistedInject
    EtcdImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    EtcdImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", getAddress())
                .append("prefix", getPrefix()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            setAddress(v.toString());
        }
        v = args.get("prefix");
        if (v != null) {
            setPrefix(v.toString());
        }
    }

}
