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
package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source;
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.DirDestinationImpl.DirDestinationImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.ServiceImpl.ServiceImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.SourceImpl.SourceImplFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * Backup service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BackupImpl implements Backup {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BackupImplFactory extends HostServiceService {

    }

    private final BackupImplLogger log;

    private HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final List<ClusterHost> clusters;

    private Service service;

    private Destination destination;

    @Inject
    private transient ServiceImplFactory serviceFactory;

    @Inject
    private transient DirDestinationImplFactory dirDestinationFactory;

    @Inject
    private transient ClientImplFactory clientFactory;

    private Client client;

    @Inject
    private transient SourceImplFactory sourceFactory;

    private final List<Source> sources;

    @Inject
    BackupImpl(BackupImplLogger log, HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.sources = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * property << 'name=value'
     * </pre>
     */
    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    /**
     * <pre>
     * service namespace: "wordpress", name: "db"
     * </pre>
     */
    public void service(Map<String, Object> args) {
        Service service = serviceFactory.create(args);
        log.serviceSet(this, service);
        this.service = service;
    }

    /**
     * <pre>
     * destination dir: "/mnt/backup"
     * </pre>
     */
    public Destination destination(Map<String, Object> args) {
        Object v = args.get("dir");
        Destination dest = null;
        if (v != null) {
            dest = dirDestinationFactory.create(args);
            log.destinationSet(this, dest);
            this.destination = dest;
        }
        return dest;
    }

    /**
     * <pre>
     * client key: "id_rsa", config: "..."
     * </pre>
     */
    public Client client(Map<String, Object> args) {
        Client client = clientFactory.create(args);
        this.client = client;
        log.clientSet(this, client);
        return client;
    }

    /**
     * <pre>
     * source "/data"
     * </pre>
     */
    public Source source(String source) {
        Map<String, Object> args = new HashMap<>();
        args.put("source", source);
        return source(args);
    }

    /**
     * <pre>
     * source source: "/data"
     * </pre>
     */
    public Source source(Map<String, Object> args) {
        Source source = sourceFactory.create(args);
        this.sources.add(source);
        return source;
    }

    /**
     * <pre>
     * source << "/data"
     * </pre>
     */
    public List<String> getSource() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                source(property);
            }
        });
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    public void addTargets(List<TargetHost> list) {
        this.targets.addAll(list);
    }

    @Override
    public List<TargetHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public ClusterHost getClusterHost() {
        return getClusterHosts().get(0);
    }

    public void addClusterHosts(List<ClusterHost> list) {
        this.clusters.addAll(list);
        log.clustersAdded(this, list);
    }

    @Override
    public List<ClusterHost> getClusterHosts() {
        return Collections.unmodifiableList(clusters);
    }

    public void setServiceProperties(HostServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "backup";
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public Destination getDestination() {
        return destination;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public List<Source> getSources() {
        return sources;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets())
                .append("clusters", getClusterHosts()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseClusters(args);
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        assertThat("targets=null", v, notNullValue());
        addTargets((List<TargetHost>) v);
    }

    @SuppressWarnings("unchecked")
    private void parseClusters(Map<String, Object> args) {
        Object v = args.get("clusters");
        assertThat("clusters=null", v, notNullValue());
        addClusterHosts((List<ClusterHost>) v);
    }

}
