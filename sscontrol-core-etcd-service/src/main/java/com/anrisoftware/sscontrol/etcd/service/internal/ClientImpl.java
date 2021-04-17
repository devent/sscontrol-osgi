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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Client;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClientImpl implements Client {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ClientImplFactory {

        Client create(@Assisted Map<String, Object> args);
    }

    private transient TlsFactory tlsFactory;

    private transient ClientImplLogger log;

    private Tls tls;

    @Inject
    ClientImpl(ClientImplLogger log, BindingFactory bindingFactory,
            TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.log = log;
        this.tls = tlsFactory.create(args);
        this.tlsFactory = tlsFactory;
        parseArgs(args);
    }

    /**
     * <pre>
     * client ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
    }

}
