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
package com.anrisoftware.sscontrol.k8smaster.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
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

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.k8sbase.base.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.external.K8s;
import com.anrisoftware.sscontrol.k8sbase.base.external.K8sService;
import com.anrisoftware.sscontrol.k8sbase.base.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sImpl.K8sImplFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Account;
import com.anrisoftware.sscontrol.k8smaster.external.Authentication;
import com.anrisoftware.sscontrol.k8smaster.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Authorization;
import com.anrisoftware.sscontrol.k8smaster.external.AuthorizationFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Binding;
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster;
import com.anrisoftware.sscontrol.k8smaster.internal.AccountImpl.AccountImplFactory;
import com.anrisoftware.sscontrol.k8smaster.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Master</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sMasterImpl implements K8sMaster {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sMasterImplFactory extends K8sService {

    }

    private final K8sMasterImplLogger log;

    @Inject
    private transient Map<String, AuthenticationFactory> authenticationFactories;

    @Inject
    private transient Map<String, AuthorizationFactory> authorizationFactories;

    private final List<Authentication> authentications;

    private final List<Authorization> authorizations;

    private final List<String> admissions;

    private Binding binding;

    private transient BindingImplFactory bindingFactory;

    private transient AccountImplFactory accountFactory;

    private Account account;

    private final K8s k8s;

    @Inject
    K8sMasterImpl(K8sMasterImplLogger log, K8sImplFactory k8sFactory,
            BindingImplFactory bindingFactory,
            AccountImplFactory accountFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.k8s = (K8s) k8sFactory.create(args);
        this.authentications = new ArrayList<>();
        this.authorizations = new ArrayList<>();
        this.admissions = new ArrayList<>();
        this.binding = bindingFactory.create();
        this.bindingFactory = bindingFactory;
        this.accountFactory = accountFactory;
        this.account = accountFactory.create();
    }

    /**
     * <pre>
     * property << 'name=value'
     * </pre>
     */
    @Override
    public List<String> getProperty() {
        return k8s.getProperty();
    }

    /**
     * <pre>
     * target name: 'master'
     * </pre>
     */
    @Override
    public void target(Map<String, Object> args) {
        k8s.target(args);
    }

    /**
     * <pre>
     * debug "error", level: 1
     * </pre>
     */
    @Override
    public void debug(Map<String, Object> args, String name) {
        k8s.debug(args, name);
    }

    /**
     * <pre>
     * debug name: "error", level: 1
     * </pre>
     */
    @Override
    public void debug(Map<String, Object> args) {
        k8s.debug(args);
    }

    /**
     * <pre>
     * debug << [name: "error", level: 1]
     * </pre>
     */
    @Override
    public List<Object> getDebug() {
        return k8s.getDebug();
    }

    /**
     * <pre>
     * bind insecure: "127.0.0.1", secure: "0.0.0.0", port: 8080
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        this.binding = bindingFactory.create(a);
        log.bindingSet(this, binding);
    }

    /**
     * <pre>
     * cluster range: "10.254.0.0/16"
     * </pre>
     */
    @Override
    public void cluster(Map<String, Object> args) {
        k8s.cluster(args);
    }

    /**
     * <pre>
     * plugin "etcd"
     * </pre>
     */
    @Override
    public Plugin plugin(String name) {
        return k8s.plugin(name);
    }

    /**
     * <pre>
     * plugin "etcd", target: "infra0"
     * </pre>
     */
    @Override
    public Plugin plugin(Map<String, Object> args, String name) {
        return k8s.plugin(args, name);
    }

    /**
     * <pre>
     * plugin name: "etcd", target: "infra0"
     * </pre>
     */
    @Override
    public Plugin plugin(Map<String, Object> args) {
        return k8s.plugin(args);
    }

    /**
     * <pre>
     * privileged true
     * </pre>
     */
    @Override
    public void privileged(boolean allow) {
        k8s.privileged(allow);
    }

    /**
     * <pre>
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    @Override
    public void tls(Map<String, Object> args) {
        k8s.tls(args);
    }

    /**
     * <pre>
     * kubelet port: 10250
     * </pre>
     */
    @Override
    public Kubelet kubelet(Map<String, Object> args) {
        return k8s.kubelet(args);
    }

    /**
     * <pre>
     * authentication "basic", file: "some_file"
     * </pre>
     */
    public void authentication(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("type", name);
        authentication(a);
    }

    /**
     * <pre>
     * authentication type: "basic", file: "some_file"
     * </pre>
     */
    public void authentication(Map<String, Object> args) {
        String name = args.get("type").toString();
        AuthenticationFactory factory = authenticationFactories.get(name);
        assertThat(format("authentication(%s)=null", name), factory,
                is(notNullValue()));
        Authentication auth = factory.create(args);
        authentications.add(auth);
        log.authenticationAdded(this, auth);
    }

    /**
     * <pre>
     * authorization "allow"
     * </pre>
     */
    public void authorization(String name) {
        Map<String, Object> a = new HashMap<>();
        a.put("mode", name);
        authorization(a);
    }

    /**
     * <pre>
     * authorization "abac", file: "policy_file.json"
     * </pre>
     */
    public void authorization(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("mode", name);
        authorization(a);
    }

    /**
     * <pre>
     * authorization mode: "abac", abac: ""
     * </pre>
     */
    public void authorization(Map<String, Object> args) {
        String name = args.get("mode").toString();
        AuthorizationFactory factory = authorizationFactories.get(name);
        assertThat(format("authorization(%s)=null", name), factory,
                is(notNullValue()));
        Authorization auth = factory.create(args);
        authorizations.add(auth);
        log.authorizationAdded(this, auth);
    }

    /**
     * <pre>
     * admission << "AlwaysAdmit,ServiceAccount"
     * </pre>
     */
    public List<String> getAdmission() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                String[] split = StringUtils.split(property, ",");
                ToList.toList(admissions, split);
                log.admissionsAdded(K8sMasterImpl.this, property);
            }
        });
    }

    /**
     * <pre>
     * account ca: 'ca.pem', key: 'ca_key.pem' // or
     * </pre>
     */
    public void account(Map<String, Object> args) {
        this.account = accountFactory.create(args);
        log.accountSet(this, account);
    }

    @Override
    public DebugLogging getDebugLogging() {
        return k8s.getDebugLogging();
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public void addTargets(List<SshHost> list) {
        k8s.addTargets(list);
    }

    @Override
    public List<SshHost> getTargets() {
        return k8s.getTargets();
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return k8s.getServiceProperties();
    }

    @Override
    public String getName() {
        return "k8s-master";
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public Cluster getCluster() {
        return k8s.getCluster();
    }

    @Override
    public Map<String, Plugin> getPlugins() {
        return k8s.getPlugins();
    }

    @Override
    public Boolean isAllowPrivileged() {
        return k8s.isAllowPrivileged();
    }

    @Override
    public List<Authentication> getAuthentications() {
        return authentications;
    }

    @Override
    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    @Override
    public Kubelet getKubelet() {
        return k8s.getKubelet();
    }

    @Override
    public List<String> getAdmissions() {
        return admissions;
    }

    @Override
    public Tls getTls() {
        return k8s.getTls();
    }

    @Override
    public void setContainerRuntime(String runtime) {
        k8s.setContainerRuntime(runtime);
    }

    @Override
    public String getContainerRuntime() {
        return k8s.getContainerRuntime();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets()).toString();
    }

}
