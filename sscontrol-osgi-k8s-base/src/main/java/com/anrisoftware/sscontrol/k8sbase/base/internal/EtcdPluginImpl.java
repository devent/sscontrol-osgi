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
package com.anrisoftware.sscontrol.k8sbase.base.internal;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.external.EtcdPlugin;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Etcd</i> plugin.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class EtcdPluginImpl implements EtcdPlugin {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface EtcdPluginImplFactory extends PluginFactory {

    }

    private final List<Object> targets;

    private final List<String> addresses;

    private transient TlsFactory tlsFactory;

    private Tls tls;

    private Integer port;

    private String protocol;

    @AssistedInject
    EtcdPluginImpl(TlsFactory tlsFactory) {
        this(tlsFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    EtcdPluginImpl(TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        this.targets = new ArrayList<>();
        this.addresses = new ArrayList<>();
        parseArgs(args);
    }

    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
    }

    @Override
    public String getName() {
        return "etcd";
    }

    public void addTargets(String[] list) {
        addTargets(Arrays.asList(list));
    }

    public void addTargets(List<?> list) {
        targets.addAll(list);
    }

    public void addTarget(Object target) {
        targets.add(target);
    }

    @Override
    public List<Object> getTarget() {
        return targets;
    }

    public void addAddresses(String[] list) {
        addAddresses(Arrays.asList(list));
    }

    public void addAddresses(List<String> list) {
        addresses.addAll(list);
    }

    public void addAddress(String address) {
        addresses.add(address);
    }

    @Override
    public List<String> getAddress() {
        return addresses;
    }

    public void setTls(Tls tls) {
        this.tls = tls;
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("target");
        if (v != null) {
            setTargets(v);
        }
        v = args.get("address");
        if (v != null) {
            getAddresses(v);
        }
    }

    @SuppressWarnings("unchecked")
    private void getAddresses(Object v) {
        if (v instanceof List) {
            addAddresses((List<String>) v);
        } else {
            addAddresses(split(v.toString(), ","));
        }
    }

    private void setTargets(Object v) {
        if (v instanceof List) {
            addTargets((List<?>) v);
        } else {
            addTargets(split(v.toString(), ","));
        }
    }

}
