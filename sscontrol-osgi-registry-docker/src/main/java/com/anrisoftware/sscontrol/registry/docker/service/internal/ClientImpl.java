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
package com.anrisoftware.sscontrol.registry.docker.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.registry.docker.service.external.Client;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Client certificates.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ClientImpl implements Client {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClientImplFactory {

        Client create(Map<String, Object> args);

    }

    private transient TlsFactory tlsFactory;

    private Tls tls;

    @Inject
    ClientImpl(TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        parseArgs(args);
    }

    @Override
    public URI getCa() {
        return tls.getCa();
    }

    @Override
    public URI getCert() {
        return tls.getCert();
    }

    @Override
    public URI getKey() {
        return tls.getKey();
    }

    @Override
    public String getCaName() {
        return tls.getCaName();
    }

    @Override
    public String getCertName() {
        return tls.getCertName();
    }

    @Override
    public String getKeyName() {
        return tls.getKeyName();
    }

    public void setCaName(String caName) {
        tls.setCaName(caName);
    }

    public void setCertName(String certName) {
        tls.setCertName(certName);
    }

    public void setKeyName(String keyName) {
        tls.setKeyName(keyName);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
    }

}
