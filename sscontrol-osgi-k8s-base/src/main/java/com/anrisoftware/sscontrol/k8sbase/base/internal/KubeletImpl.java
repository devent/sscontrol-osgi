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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.k8sbase.base.external.Kubelet;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

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

        Kubelet create(Map<String, Object> args);
    }

    @Inject
    private transient KubeletImplLogger log;

    private Tls tls;

    private transient TlsFactory tlsFactory;

    private final List<String> preferredAddressTypes;

    private Integer port;

    @AssistedInject
    KubeletImpl(TlsFactory tlsFactory) {
        this(tlsFactory, new HashMap<>());
    }

    @AssistedInject
    KubeletImpl(TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create(new HashMap<String, Object>());
        this.preferredAddressTypes = new ArrayList<>();
        parseArgs(args);
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public List<String> getPreferredAddressTypes() {
        return preferredAddressTypes;
    }

    public void setPort(int port) {
        assertThat("port>0", port, greaterThan(0));
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
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

    /**
     * <pre>
     * bind port: 10250
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Object v = args.get("port");
        Integer port = (Integer) v;
        setPort(port);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("port");
        if (v != null) {
            setPort((int) v);
        }
    }

}
