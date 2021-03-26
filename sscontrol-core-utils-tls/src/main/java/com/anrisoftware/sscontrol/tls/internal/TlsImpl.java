/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.tls.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURIFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * TLS certificates.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class TlsImpl implements Tls {

    private URI ca;

    private URI cert;

    private URI key;

    private String caName;

    private String certName;

    private String keyName;

    @AssistedInject
    TlsImpl(ToURIFactory touri) {
        this(new HashMap<String, Object>(), touri);
    }

    @AssistedInject
    TlsImpl(@Assisted Map<String, Object> args, ToURIFactory touri) {
        Object v = args.get("ca");
        if (v != null) {
            assertThat("ca=null", v.toString(), not(equalTo("null")));
            this.ca = touri.create(v).convert();
        } else {
            this.ca = null;
        }
        v = args.get("caName");
        if (v != null) {
            this.caName = v.toString();
        }
        v = args.get("cert");
        if (v != null) {
            assertThat("cert=null", v.toString(), not(equalTo("null")));
            this.cert = touri.create(v).convert();
        } else {
            this.cert = null;
        }
        v = args.get("certName");
        if (v != null) {
            this.certName = v.toString();
        }
        v = args.get("key");
        if (v != null) {
            assertThat("key=null", v.toString(), not(equalTo("null")));
            this.key = touri.create(v).convert();
        } else {
            this.key = null;
        }
        v = args.get("keyName");
        if (v != null) {
            this.keyName = v.toString();
        }
    }

    public void setCa(URI ca) {
        this.ca = ca;
    }

    @Override
    public URI getCa() {
        return ca;
    }

    @Override
    public void setCaName(String caName) {
        this.caName = caName;
    }

    @Override
    public String getCaName() {
        return caName;
    }

    public void setCert(URI cert) {
        this.cert = cert;
    }

    @Override
    public URI getCert() {
        return cert;
    }

    @Override
    public void setCertName(String certName) {
        this.certName = certName;
    }

    @Override
    public String getCertName() {
        return certName;
    }

    public void setKey(URI key) {
        this.key = key;
    }

    @Override
    public URI getKey() {
        return key;
    }

    @Override
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String getKeyName() {
        return keyName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
