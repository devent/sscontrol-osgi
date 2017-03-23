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
package com.anrisoftware.sscontrol.k8sbase.base.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.external.Cluster;
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

    }

    private String serviceRange;

    private String advertiseAddress;

    private String dnsAddress;

    private final List<Object> apiServers;

    private String hostnameOverride;

    private String podRange;

    private final ClusterImplLogger log;

    @AssistedInject
    ClusterImpl(ClusterImplLogger log) {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        this.apiServers = new ArrayList<>();
        parseArgs(args);
    }

    public void setAdvertiseAddress(String advertise) {
        this.advertiseAddress = advertise;
        log.advertiseAddressSet(this, advertise);
    }

    @Override
    public String getAdvertiseAddress() {
        return advertiseAddress;
    }

    public void setDnsAddress(String address) {
        this.dnsAddress = address;
        log.dnsAddressSet(this, address);
    }

    @Override
    public String getDnsAddress() {
        return dnsAddress;
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

    public void setHostnameOverride(String hostname) {
        this.hostnameOverride = hostname;
        log.hostnameOverrideSet(this, hostname);
    }

    @Override
    public String getHostnameOverride() {
        return hostnameOverride;
    }

    public void setPodRange(String range) {
        this.podRange = range;
        log.podRangeSet(this, range);
    }

    @Override
    public String getPodRange() {
        return podRange;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("service");
        if (v != null) {
            setServiceRange(v.toString());
        }
        v = args.get("advertise");
        if (v != null) {
            setAdvertiseAddress(v.toString());
        }
        v = args.get("hostname");
        if (v != null) {
            setHostnameOverride(v.toString());
        }
        v = args.get("pod");
        if (v != null) {
            setPodRange(v.toString());
        }
        v = args.get("dns");
        if (v != null) {
            setDnsAddress(v.toString());
        }
        v = args.get("api");
        if (v != null) {
            if (v instanceof List) {
                addAllApiServer((List<?>) v);
            } else {
                addApiServer(v);
            }
        }
    }

}
