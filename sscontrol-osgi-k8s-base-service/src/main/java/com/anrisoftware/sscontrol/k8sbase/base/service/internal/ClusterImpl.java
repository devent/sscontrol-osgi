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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.Cluster;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * K8s cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClusterImpl implements Cluster {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClusterImplFactory {

        Cluster create();

        Cluster create(@Assisted Map<String, Object> args);

        Cluster create(Cluster cluster, Map<String, Object> a);

    }

    private String name;

    private final List<Object> apiServers;

    private String serviceRange;

    private Object advertiseAddress;

    private String dnsDomain;

    private String podRange;

    private final ClusterImplLogger log;

    private String protocol;

    private Integer port;

    private String joinCommand;

    @AssistedInject
    ClusterImpl(ClusterImplLogger log) {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, @Assisted Map<String, Object> args) {
        this(log, null, args);
    }

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, @Assisted Cluster cluster,
            @Assisted Map<String, Object> args) {
        this.log = log;
        if (cluster != null) {
            this.name = cluster.getName();
            this.apiServers = new ArrayList<>(cluster.getApiServers());
            this.serviceRange = cluster.getServiceRange();
            this.dnsDomain = cluster.getDnsDomain();
            this.podRange = cluster.getPodRange();
            this.protocol = cluster.getProtocol();
            this.port = cluster.getPort();
            this.joinCommand = cluster.getJoinCommand();
        } else {
            this.apiServers = new ArrayList<>();
        }
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setAdvertiseAddress(Object advertise) {
        this.advertiseAddress = advertise;
        log.advertiseAddressSet(this, advertise);
    }

    @Override
    public Object getAdvertiseAddress() {
        return advertiseAddress;
    }

    public void setDnsDomain(String domain) {
        this.dnsDomain = domain;
        log.dnsDomainSet(this, domain);
    }

    @Override
    public String getDnsDomain() {
        return dnsDomain;
    }

    @Override
    public List<Object> getApiServers() {
        return apiServers;
    }

    public void addApiServer(Object server) {
        apiServers.add(server);
        log.apiServersAdded(this, server);
    }

    public void addAllApiServer(List<?> list) {
        apiServers.addAll(list);
    }

    public void setServiceRange(String range) {
        this.serviceRange = range;
        log.serviceRangeSet(this, range);
    }

    @Override
    public String getServiceRange() {
        return serviceRange;
    }

    public void setPodRange(String range) {
        this.podRange = range;
        log.podRangeSet(this, range);
    }

    @Override
    public String getPodRange() {
        return podRange;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setJoinCommand(String joinCommand) {
        this.joinCommand = joinCommand;
    }

    @Override
    public String getJoinCommand() {
        return joinCommand;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
        v = args.get("service");
        if (v != null) {
            setServiceRange(v.toString());
        }
        v = args.get("advertise");
        if (v != null) {
            setAdvertiseAddress(v);
        }
        v = args.get("pod");
        if (v != null) {
            setPodRange(v.toString());
        }
        v = args.get("domain");
        if (v != null) {
            setDnsDomain(v.toString());
        }
        v = args.get("api");
        if (v != null) {
            if (v instanceof List) {
                addAllApiServer((List<?>) v);
            } else {
                addApiServer(v);
            }
        }
        v = args.get("join");
        if (v != null) {
            setJoinCommand(v.toString());
        }
    }

}
