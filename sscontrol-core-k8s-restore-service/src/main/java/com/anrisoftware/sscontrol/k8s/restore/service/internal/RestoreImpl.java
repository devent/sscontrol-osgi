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
package com.anrisoftware.sscontrol.k8s.restore.service.internal;

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
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.DirSourceImpl.DirSourceImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.ServiceImpl.ServiceImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.SourceImpl.SourceImplFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil.GeneticListProperty;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * Restore service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RestoreImpl implements Restore {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface RestoreImplFactory extends HostServiceService {

    }

    private final RestoreImplLogger log;

    private HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final List<ClusterHost> clusters;

    private Service service;

    private Destination origin;

    @Inject
    private transient ServiceImplFactory serviceFactory;

    @Inject
    private transient DirSourceImplFactory dirOriginFactory;

    @Inject
    private transient ClientImplFactory clientFactory;

    private Client client;

    @Inject
    private transient SourceImplFactory sourceFactory;

    private final List<Source> sources;

	private boolean dryrun;

    @Inject
    RestoreImpl(RestoreImplLogger log, HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.sources = new ArrayList<>();
		this.dryrun = false;
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
     * origin dir: "/mnt/backup"
     * </pre>
     */
    public Destination origin(Map<String, Object> args) {
        Object v = args.get("dir");
        Destination dest = null;
        if (v != null) {
            dest = dirOriginFactory.create(args);
            log.originSet(this, dest);
            this.origin = dest;
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
     * source target: "/data", chown: "33.33"
     * </pre>
     */
    public Source source(Map<String, Object> args) {
        Source source = sourceFactory.create(args);
        this.sources.add(source);
        return source;
    }

    /**
     * <pre>
     * source << [target: "/data", chown: "33.33"]
     * </pre>
     */
    public List<Map<String, Object>> getSource() {
        return GeneticListPropertyUtil
                .<Map<String, Object>>geneticListStatement(
                        new GeneticListProperty<Map<String, Object>>() {
                            @Override
                            public void add(Map<String, Object> property) {
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
        return "restore";
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public Destination getOrigin() {
        return origin;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public List<Source> getSources() {
        return sources;
    }

	public void setDryrun(boolean dryrun) {
		this.dryrun = dryrun;
		log.dryrunSet(this, dryrun);
	}

    @Override
	public boolean getDryrun() {
		return dryrun;
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
		parseDryrun(args);
	}

	private void parseDryrun(Map<String, Object> args) {
		Object v = args.get("dryrun");
		if (v != null) {
			setDryrun((boolean) v);
		}
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
