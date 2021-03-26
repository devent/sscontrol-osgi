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
package com.anrisoftware.sscontrol.etcd.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.etcd.service.external.ClientCertsAuthentication;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * X509 Client Certs.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ClientCertsAuthenticationImpl
        implements ClientCertsAuthentication {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ClientCertsAuthenticationImplFactory
            extends AuthenticationFactory {

    }

    private final Tls tls;

    @Inject
    ClientCertsAuthenticationImpl(TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
    }

    public Tls getTls() {
        return tls;
    }

    @Override
    public String getType() {
        return "cert";
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
