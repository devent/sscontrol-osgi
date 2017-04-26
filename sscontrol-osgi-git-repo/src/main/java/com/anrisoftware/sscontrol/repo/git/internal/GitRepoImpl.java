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
package com.anrisoftware.sscontrol.repo.git.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
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

import com.anrisoftware.sscontrol.repo.git.external.Credentials;
import com.anrisoftware.sscontrol.repo.git.external.GitRepo;
import com.anrisoftware.sscontrol.repo.git.external.GitRepoHost;
import com.anrisoftware.sscontrol.repo.git.external.Remote;
import com.anrisoftware.sscontrol.repo.git.internal.GitRepoHostImpl.GitRepoHostImplFactory;
import com.anrisoftware.sscontrol.repo.git.internal.RemoteImpl.RemoteImplFactory;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.external.host.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.host.HostServiceService;
import com.anrisoftware.sscontrol.types.external.repo.RepoHost;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
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
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface GitRepoImplFactory extends HostServiceService {

    }

    private final GitRepoImplLogger log;

    @Inject
    private transient Map<String, CredentialsFactory> credentialsFactories;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> targets;

    private final GitRepoHostImplFactory hostFactory;

    private Credentials credentials;

    private String group;

    private Remote remote;

    private final RemoteImplFactory remoteFactory;

    @Inject
    GitRepoImpl(GitRepoImplLogger log, HostPropertiesService propertiesService,
            GitRepoHostImplFactory hostFactory, RemoteImplFactory remoteFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.hostFactory = hostFactory;
        this.remoteFactory = remoteFactory;
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
        List<SshHost> l = InvokerHelper.asList(v);
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

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    public void addTargets(List<SshHost> list) {
        this.targets.addAll(list);
    }

    @Override
    public List<SshHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "git";
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
    public List<RepoHost> getHosts() {
        List<RepoHost> list = new ArrayList<>();
        for (SshHost ssh : targets) {
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
            targets.addAll((List<SshHost>) v);
        }
    }

    private void parseGroup(Map<String, Object> args) {
        Object v = args.get("group");
        if (v != null) {
            this.group = v.toString();
        }
    }

}
