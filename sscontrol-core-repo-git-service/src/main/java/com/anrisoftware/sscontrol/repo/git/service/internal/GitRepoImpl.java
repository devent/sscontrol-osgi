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
package com.anrisoftware.sscontrol.repo.git.service.internal;

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
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.repo.git.service.external.Checkout;
import com.anrisoftware.sscontrol.repo.git.service.external.Credentials;
import com.anrisoftware.sscontrol.repo.git.service.external.GitRepo;
import com.anrisoftware.sscontrol.repo.git.service.external.GitRepoHost;
import com.anrisoftware.sscontrol.repo.git.service.external.Remote;
import com.anrisoftware.sscontrol.repo.git.service.internal.CheckoutImpl.CheckoutImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoHostImpl.GitRepoHostImplFactory;
import com.anrisoftware.sscontrol.repo.git.service.internal.RemoteImpl.RemoteImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class GitRepoImpl implements GitRepo {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface GitRepoImplFactory extends HostServiceService {

    }

    private final GitRepoImplLogger log;

    @Inject
    private transient Map<String, CredentialsFactory> credentialsFactories;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final GitRepoHostImplFactory hostFactory;

    private Credentials credentials;

    private String group;

    private Remote remote;

    private final RemoteImplFactory remoteFactory;

    private transient CheckoutImplFactory checkoutFactory;

    private Checkout checkout;

    @Inject
    GitRepoImpl(GitRepoImplLogger log, HostServicePropertiesService propertiesService,
            GitRepoHostImplFactory hostFactory, RemoteImplFactory remoteFactory,
            CheckoutImplFactory checkoutFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.hostFactory = hostFactory;
        this.remoteFactory = remoteFactory;
        this.checkoutFactory = checkoutFactory;
        this.checkout = checkoutFactory.create(new HashMap<String, Object>());
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
     * target "default"
     * </pre>
     */
    public void target(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("target", name);
        target(args);
    }

    /**
     * <pre>
     * target name: "default"
     * </pre>
     */
    public void target(Map<String, Object> args) {
        Object v = args.get("target");
        @SuppressWarnings("unchecked")
        List<TargetHost> l = InvokerHelper.asList(v);
        targets.addAll(l);
    }

    /**
     * <pre>
     * group "wordpress-app"
     * </pre>
     */
    public void group(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("group", name);
        group(args);
    }

    /**
     * <pre>
     * group name: "wordpress-app"
     * </pre>
     */
    public void group(Map<String, Object> args) {
        Object v = args.get("group");
        this.group = v.toString();
    }

    /**
     * <pre>
     * remote "git://git@github.com/fluentd-logging"
     * </pre>
     */
    public void remote(String name) {
        Map<String, Object> args = new HashMap<>();
        args.put("url", name);
        remote(args);
    }

    /**
     * <pre>
     * remote url: "git://git@github.com/fluentd-logging"
     * </pre>
     */
    public void remote(Map<String, Object> args) {
        this.remote = remoteFactory.create(args);
        log.remoteSet(this, remote);
    }

    /**
     * <pre>
     * credentials "ssh", key: "file://id_rsa.pub"
     * </pre>
     */
    public void credentials(Map<String, Object> args, String type) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("type", type);
        credentials(a);
    }

    /**
     * <pre>
     * credentials type: "ssh", key: "file://id_rsa.pub"
     * </pre>
     */
    public void credentials(Map<String, Object> args) {
        Object type = args.get("type");
        assertThat("type=null", type, notNullValue());
        CredentialsFactory factory = credentialsFactories.get(type.toString());
        Credentials c = factory.create(args);
        this.credentials = c;
        log.credentialsSet(this, c);
    }

    /**
     * <pre>
     * checkout tag: "aaa", branch: "foo", commit: "e9edddc2e2a59ecb5526febf5044828e7fedd914"
     * </pre>
     */
    public void checkout(Map<String, Object> args) {
        this.checkout = checkoutFactory.create(args);
        log.checkoutSet(this, checkout);
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
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "repo-git";
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public Remote getRemote() {
        return remote;
    }

    @Override
    public Checkout getCheckout() {
        return checkout;
    }

    @Override
    public List<RepoHost> getHosts() {
        List<RepoHost> list = new ArrayList<>();
        for (TargetHost ssh : targets) {
            GitRepoHost host;
            host = hostFactory.create(this, ssh);
            list.add(host);
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseGroup(args);
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

    private void parseGroup(Map<String, Object> args) {
        Object v = args.get("group");
        if (v != null) {
            this.group = v.toString();
        }
    }

}
