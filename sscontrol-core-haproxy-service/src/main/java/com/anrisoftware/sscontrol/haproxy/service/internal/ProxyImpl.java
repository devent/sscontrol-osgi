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
package com.anrisoftware.sscontrol.haproxy.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.haproxy.service.external.Backend;
import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy;
import com.anrisoftware.sscontrol.haproxy.service.internal.BackendImpl.BackendImplFactory;
import com.anrisoftware.sscontrol.haproxy.service.internal.FrontendImpl.FrontendImplFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * HAProxy proxy.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ProxyImpl implements Proxy {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ProxyImplFactory {

        Proxy create(Map<String, Object> args);
    }

    private transient ProxyImplLogger log;

    @Inject
    private transient BackendImplFactory backendFactory;

    @Inject
    private transient FrontendImplFactory frontendFactory;

    private String name;

    private Frontend frontend;

    private Backend backend;

    @Inject
    ProxyImpl(ProxyImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseName(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name != null", v, notNullValue());
        this.name = v.toString();
    }

    /**
     * <pre>
     * frontend name: "foo", address: "192.168.56.201", port: 443
     * </pre>
     */
    public void frontend(Map<String, Object> args) {
        Frontend frontend = frontendFactory.create(args);
        this.frontend = frontend;
        log.frontendSet(this, frontend);
    }

    /**
     * <pre>
     * backend address: "192.168.56.201", port: 30001
     * </pre>
     */
    public void backend(Map<String, Object> args) {
        Backend backend = backendFactory.create(args);
        this.backend = backend;
        log.backendSet(this, backend);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setFrontend(Frontend target) {
        this.frontend = target;
        log.frontendSet(this, target);
    }

    @Override
    public Frontend getFrontend() {
        return frontend;
    }
    
    public void setBackend(Backend backend) {
        this.backend = backend;
    }
    
    @Override
    public Backend getBackend() {
        return backend;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
