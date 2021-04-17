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
package com.anrisoftware.sscontrol.registry.docker.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.registry.docker.service.external.Host;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Repository remote host.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class HostImpl implements Host {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface HostImplFactory {

        Host create(Map<String, Object> args);

    }

    private URI address;

    private transient HostImplLogger log;

    private transient TlsFactory tlsFactory;

    private Tls client;

    @Inject
    HostImpl(HostImplLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.tlsFactory = tlsFactory;
        this.client = tlsFactory.create();
        parseArgs(args);
    }

    public void setAddress(URI address) {
        this.address = address;
        log.addressSet(this, address);
    }

    @Override
    public URI getAddress() {
        return address;
    }

    @Override
    public Tls getClient() {
        return client;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        setAddress(ToURI.toURI(v).convert());
        if (args.containsKey("ca") || args.containsKey("cert")
                || args.containsKey("key")) {
            this.client = tlsFactory.create(args);
        }
    }

}
