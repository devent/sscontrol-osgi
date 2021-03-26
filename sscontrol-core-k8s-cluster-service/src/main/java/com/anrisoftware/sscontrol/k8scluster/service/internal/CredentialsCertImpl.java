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
package com.anrisoftware.sscontrol.k8scluster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8scluster.service.external.CredentialsCertFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.cluster.external.CredentialsCert;
import com.google.inject.assistedinject.Assisted;

/**
 * Certificate based credentials.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CredentialsCertImpl extends AbstractCredentials
        implements CredentialsCert {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface CredentialsCertImplFactory extends CredentialsCertFactory {

    }

    private Tls tls;

    @Inject
    CredentialsCertImpl(AbstractCredentialsLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        super(log, args);
        this.tls = tlsFactory.create(args);
        parseArgs(args, tlsFactory);
    }

    @Override
    public String getType() {
        return "cert";
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    private void parseArgs(Map<String, Object> args, TlsFactory tlsFactory) {
        Object ca = args.get("ca");
        Object cert = args.get("cert");
        Object key = args.get("key");
        if (ca != null || cert != null || key != null) {
            this.tls = tlsFactory.create(args);
        }
    }

}
