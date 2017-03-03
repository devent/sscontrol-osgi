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
package com.anrisoftware.sscontrol.k8snode.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8snode.external.Master;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Kubernetes master.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class MasterImpl implements Master {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface MasterImplFactory {

        Master create();

        Master create(Map<String, Object> args);
    }

    private String target;

    private String address;

    private String protocol;

    private Integer port;

    private transient TlsFactory tlsFactory;

    private Tls tls;

    @AssistedInject
    MasterImpl(TlsFactory tlsFactory) {
        this(tlsFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    MasterImpl(TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        parseArgs(args);
    }

    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setTls(Tls tls) {
        this.tls = tls;
    }

    public Tls getTls() {
        return tls;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("target");
        if (v != null) {
            this.target = v.toString();
        }
        v = args.get("address");
        if (v != null) {
            this.address = v.toString();
        }
        v = args.get("protocol");
        if (v != null) {
            this.protocol = v.toString();
        }
        v = args.get("port");
        if (v != null) {
            this.port = (Integer) v;
        }
    }

}
