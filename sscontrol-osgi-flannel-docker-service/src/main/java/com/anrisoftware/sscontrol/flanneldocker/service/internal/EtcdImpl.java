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

import static org.apache.commons.lang3.StringUtils.split;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Etcd;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
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

    private final List<Object> endpoints;

    private String prefix;

    private final TlsFactory tlsFactory;

    private Tls tls;

    private final EtcdImplLogger log;

    private Object address;

    @AssistedInject
    EtcdImpl(EtcdImplLogger log, TlsFactory tlsFactory) {
        this(log, tlsFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    EtcdImpl(EtcdImplLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        this.log = log;
        this.endpoints = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    public void addEndpoints(List<String> endpoints) {
        this.endpoints.addAll(endpoints);
    }

    @Override
    public List<Object> getEndpoints() {
        return endpoints;
    }

    public void addEndpoints(Object endpoint) {
        endpoints.add(endpoint);
        if (address == null) {
            setAddress(endpoint);
        }
        log.endpointAdded(this, endpoint);
    }

    public void addAllEndpoints(String list) {
        addAllEndpoints(Arrays.asList(split(list, ",")));
    }

    public void addAllEndpoints(List<?> list) {
        endpoints.addAll(list);
        if (address == null) {
            setAddress(list.get(0));
        }
        log.endpointsAdded(this, list);
    }

    public void setAddress(Object address) {
        this.address = address;
        log.addressSet(this, address);
    }

    @Override
    public Object getAddress() {
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
    public Tls getTls() {
        return tls;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("endpoints", getEndpoints())
                .append("prefix", getPrefix()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("endpoints");
        if (v != null) {
            if (v instanceof List) {
                addAllEndpoints((List<?>) v);
            } else {
                addAllEndpoints(v.toString());
            }
        }
        v = args.get("address");
        if (v != null) {
            setAddress(v);
        }
        v = args.get("prefix");
        if (v != null) {
            setPrefix(v.toString());
        }
    }

}
