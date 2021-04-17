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

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.Gateway;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> gateway.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class GatewayImpl implements Gateway {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface GatewayImplFactory {

        Gateway create(@Assisted Map<String, Object> args);
    }

    private final List<String> endpoints;

    private transient GatewayImplLogger log;

    @Inject
    GatewayImpl(GatewayImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        this.endpoints = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * endpoint << "https://etcd-0:2379"
     * </pre>
     */
    public List<String> getEndpoint() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                addEndpoint(property);
            }
        });
    }

    public void addEndpoint(String endpoint) {
        endpoints.add(endpoint);
        log.advertiseAdded(this, endpoint);
    }

    @Override
    public List<String> getEndpoints() {
        return endpoints;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseEndpoints(args);
    }

    private void parseEndpoints(Map<String, Object> args) {
        Object v = args.get("endpoints");
        if (v != null) {
            addEndpoint(v.toString());
        }
    }

}
