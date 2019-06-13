/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistry;
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryHost;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Git</i> code repository host.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class DockerRegistryHostImpl implements DockerRegistryHost {

    private final DockerRegistry registry;

    private String host;

    private String proto;

    private Integer port;

    /**
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface DockerRegistryHostImplFactory {

        DockerRegistryHost create(DockerRegistry registry, TargetHost target);
    }

    @Inject
    DockerRegistryHostImpl(@Assisted DockerRegistry registry,
            @Assisted TargetHost target) {
        this.registry = registry;
        this.host = target.getHost();
        this.proto = target.getProto();
        this.port = target.getPort();
    }

    @Override
    public String getType() {
        return "docker";
    }

    @Override
    public DockerRegistry getRegistry() {
        return registry;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    @Override
    public String getProto() {
        return proto;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getHostAddress() throws UnknownHostException {
        return InetAddress.getByName(host).getHostAddress();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
