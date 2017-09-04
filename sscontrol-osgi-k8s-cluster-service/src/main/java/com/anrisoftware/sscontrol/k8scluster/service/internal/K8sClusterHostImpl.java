/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8scluster.service.external.Context;
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sCluster;
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHost;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.cluster.external.Credentials;
import com.anrisoftware.sscontrol.types.cluster.external.CredentialsCert;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s</i> cluster host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sClusterHostImpl implements K8sClusterHost {

    private final Credentials credentials;

    private final K8sCluster cluster;

    private String proto;

    private String host;

    private Integer port;

    private final Context context;

    /**
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sClusterHostImplFactory {

        K8sClusterHost create(K8sCluster cluster, TargetHost target,
                Credentials credentials, Context context);
    }

    @Inject
    K8sClusterHostImpl(@Assisted K8sCluster cluster,
            @Assisted TargetHost target, @Assisted Credentials credentials,
            @Assisted Context context) {
        this.cluster = cluster;
        this.proto = target.getProto();
        this.host = target.getHost();
        this.port = target.getPort();
        this.credentials = credentials;
        this.context = context;
    }

    @Override
    public K8sCluster getCluster() {
        return cluster;
    }

    @Override
    public String getClusterName() {
        return cluster.getCluster().getName();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    @Override
    public String getProto() {
        return proto;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
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
    public Tls getTls() {
        if (credentials instanceof CredentialsCert) {
            CredentialsCert certs = (CredentialsCert) credentials;
            return certs.getTls();
        }
        return null;
    }

    @Override
    public URI getUrl() throws URISyntaxException {
        return new URI(proto, null, host, port, null, null, null);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}