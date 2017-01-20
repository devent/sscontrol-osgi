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
package com.anrisoftware.sscontrol.k8smaster.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.resources.ToURI;
import com.anrisoftware.globalpom.resources.ToURIFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Tls;
import com.google.inject.assistedinject.Assisted;

/**
 * TLS certificates.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TlsImpl implements Tls {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TlsImplFactory {

        Tls create(@Assisted Map<String, Object> args);
    }

    private final URI ca;

    private final URI cert;

    private final URI key;

    @Inject
    TlsImpl(ToURIFactory touri, @Assisted Map<String, Object> args) {
        ToURI uri = touri.create();
        Object v = args.get("ca");
        if (v != null) {
            this.ca = uri.convert(v);
        } else {
            this.ca = null;
        }
        v = args.get("cert");
        if (v != null) {
            this.cert = uri.convert(v);
        } else {
            this.cert = null;
        }
        v = args.get("key");
        if (v != null) {
            this.key = uri.convert(v);
        } else {
            this.key = null;
        }
    }

    @Override
    public URI getCa() {
        return ca;
    }

    @Override
    public String getCaName() {
        return FilenameUtils.getName(ca.toString());
    }

    @Override
    public URI getCert() {
        return cert;
    }

    @Override
    public String getCertName() {
        return FilenameUtils.getName(cert.toString());
    }

    @Override
    public URI getKey() {
        return key;
    }

    @Override
    public String getKeyName() {
        return FilenameUtils.getName(key.toString());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
