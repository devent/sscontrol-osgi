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

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * X509 Peer Client Certs.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class PeerClientCertsAuthenticationImpl
        extends ClientCertsAuthenticationImpl {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface PeerClientCertsAuthenticationImplFactory
            extends AuthenticationFactory {

    }

    @Inject
    PeerClientCertsAuthenticationImpl(TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        super(tlsFactory, args);
    }

}
