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
package com.anrisoftware.sscontrol.docker.service.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.docker.service.external.Mirror;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Registry mirror.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class MirrorImpl implements Mirror {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface MirrorImplFactory {

        Mirror create();

        Mirror create(Map<String, Object> args);

    }

    private final MirrorImplLogger log;

    private URI host;

    private final TlsFactory tlsFactory;

    private Tls tls;

    @AssistedInject
    MirrorImpl(MirrorImplLogger log, TlsFactory tlsFactory)
            throws URISyntaxException {
        this(log, tlsFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    MirrorImpl(MirrorImplLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) throws URISyntaxException {
        this.log = log;
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        parseArgs(args);
    }

    /**
     * <pre>
     * tls ca: "ca.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    public void setHost(URI host) {
        this.host = host;
        log.hostSet(this, host);
    }

    @Override
    public URI getHost() {
        return host;
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
        Object v = args.get("host");
        if (v != null) {
            setHost(ToURI.toURI(v).withScheme("").convert());
        }
        v = args.get("ca");
        if (v != null) {
            tls(args);
        }
    }

}
