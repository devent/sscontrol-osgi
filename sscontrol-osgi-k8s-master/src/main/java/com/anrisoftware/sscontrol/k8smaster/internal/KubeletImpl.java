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
package com.anrisoftware.sscontrol.k8smaster.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.k8sbase.base.external.Binding;
import com.anrisoftware.sscontrol.k8sbase.base.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.internal.KubeletImplLogger;
import com.anrisoftware.sscontrol.k8sbase.base.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;

/**
 * Kubelet client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class KubeletImpl implements Kubelet {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface KubeletImplFactory {

        Kubelet create();
    }

    @Inject
    private transient KubeletImplLogger log;

    private Tls tls;

    private transient TlsFactory tlsFactory;

    private final List<String> preferredAddressTypes;

    private Binding binding;

    private final BindingImplFactory bindingFactory;

    @Inject
    KubeletImpl(BindingImplFactory bindingFactory, TlsFactory tlsFactory) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create(new HashMap<String, Object>());
        this.preferredAddressTypes = new ArrayList<>();
        this.binding = bindingFactory.create();
        this.bindingFactory = bindingFactory;
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public List<String> getPreferredAddressTypes() {
        return preferredAddressTypes;
    }

    /**
     * <pre>
     * bind port: 8080
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        this.binding = bindingFactory.create(a);
        log.bindingSet(this, binding);
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

    /**
     * <pre>
     * preferred types: "InternalIP,Hostname,ExternalIP"
     * </pre>
     */
    public void preferred(Map<String, Object> args) {
        Object v = args.get("types");
        if (v != null) {
            String[] split = StringUtils.split(v.toString(), ",");
            ToList.toList(preferredAddressTypes, split);
            log.preferredTypesAdded(this, v);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
