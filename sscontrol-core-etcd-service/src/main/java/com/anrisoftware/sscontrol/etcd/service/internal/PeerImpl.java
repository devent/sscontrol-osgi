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
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.Authentication;
import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Cluster;
import com.anrisoftware.sscontrol.etcd.service.external.Peer;
import com.anrisoftware.sscontrol.etcd.service.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class PeerImpl implements Peer {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface PeerImplFactory {

        Peer create(@Assisted Map<String, Object> args);
    }

    private final List<Binding> advertises;

    @Inject
    private Map<String, AuthenticationFactory> authenticationFactories;

    private final List<Authentication> authentications;

    private final BindingFactory bindingFactory;

    private final PeerImplLogger log;

    private String state;

    private String token;

    private final List<Binding> listens;

    private final List<Cluster> clusters;

    @Inject
    private ClusterImplFactory clusterFactory;

    private final TlsFactory tlsFactory;

    private Tls tls;

    @Inject
    PeerImpl(PeerImplLogger log, BindingFactory bindingFactory,
            TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.log = log;
        this.advertises = new ArrayList<>();
        this.authentications = new ArrayList<>();
        this.listens = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.bindingFactory = bindingFactory;
        this.tls = tlsFactory.create();
        this.tlsFactory = tlsFactory;
        parseArgs(args);
    }

    /**
     * <pre>
     * token "etcd-cluster-1"
     * </pre>
     */
    public void token(String token) {
        this.token = token;
        log.tokenSet(this, token);
    }

    /**
     * <pre>
     * state "new"
     * </pre>
     */
    public void state(String state) {
        this.state = state;
        log.stateSet(this, state);
    }

    /**
     * <pre>
     * advertise "https://10.0.1.10:2380"
     * </pre>
     */
    public void advertise(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        advertise(a);
    }

    /**
     * <pre>
     * advertise address: "https://10.0.1.10:2380"
     * </pre>
     */
    public void advertise(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        Binding binding = bindingFactory.create(a);
        log.advertiseAdded(this, binding);
        advertises.add(binding);
    }

    /**
     * <pre>
     * listen "https://10.0.1.10:2380"
     * </pre>
     */
    public void listen(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        listen(a);
    }

    /**
     * <pre>
     * listen address: "https://10.0.1.10:2380"
     * </pre>
     */
    public void listen(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        Binding binding = bindingFactory.create(a);
        log.listenAdded(this, binding);
        listens.add(binding);
    }

    /**
     * <pre>
     * tls cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    /**
     * <pre>
     * authentication "cert", ca: "ca.pem"
     * </pre>
     */
    public void authentication(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("type", name);
        authentication(a);
    }

    /**
     * <pre>
     * authentication type: "cert", ca: "ca.pem"
     * </pre>
     */
    public void authentication(Map<String, Object> args) {
        String name = args.get("type").toString();
        name = format("peer-%s", name);
        AuthenticationFactory factory = authenticationFactories.get(name);
        assertThat(format("authentication(%s)=null", name), factory,
                is(notNullValue()));
        Authentication auth = factory.create(args);
        authentications.add(auth);
        log.authenticationAdded(this, auth);
    }

    /**
     * <pre>
     * cluster << 'infra0=https://10.0.1.10:2380'
     * </pre>
     */
    public List<String> getCluster() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                Cluster cluster = clusterFactory.create(property);
                clusters.add(cluster);
                log.clusterAdded(PeerImpl.this, cluster);
            }
        });
    }

    /**
     * <pre>
     * cluster "infra0", address: "https://10.0.1.10:2380"
     * </pre>
     */
    public void cluster(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("name", name);
        cluster(a);
    }

    /**
     * <pre>
     * cluster name: "infra0", address: "https://10.0.1.10:2380"
     * </pre>
     */
    public void cluster(Map<String, Object> args) {
        Cluster cluster = clusterFactory.create(args);
        clusters.add(cluster);
        log.clusterAdded(this, cluster);
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public List<Binding> getListens() {
        return listens;
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public List<Binding> getAdvertises() {
        return advertises;
    }

    @Override
    public List<Cluster> getClusters() {
        return clusters;
    }

    @Override
    public List<Authentication> getAuthentications() {
        return authentications;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("token", getToken()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("token");
        if (v != null) {
            token(v.toString());
        }
        v = args.get("state");
        if (v != null) {
            state(v.toString());
        }
        v = args.get("advertise");
        if (v != null) {
            String s = v.toString();
            for (String a : StringUtils.split(s, ',')) {
                advertise(a);
            }
        }
        v = args.get("listen");
        if (v != null) {
            String s = v.toString();
            for (String a : StringUtils.split(s, ',')) {
                listen(a);
            }
        }
    }

}
