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
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugLoggingService;
import com.anrisoftware.sscontrol.etcd.service.external.Authentication;
import com.anrisoftware.sscontrol.etcd.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Client;
import com.anrisoftware.sscontrol.etcd.service.external.Etcd;
import com.anrisoftware.sscontrol.etcd.service.external.EtcdService;
import com.anrisoftware.sscontrol.etcd.service.external.Gateway;
import com.anrisoftware.sscontrol.etcd.service.external.Peer;
import com.anrisoftware.sscontrol.etcd.service.external.Proxy;
import com.anrisoftware.sscontrol.etcd.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.GatewayImpl.GatewayImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.PeerImpl.PeerImplFactory;
import com.anrisoftware.sscontrol.etcd.service.internal.ProxyImpl.ProxyImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class EtcdImpl implements Etcd {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface EtcdImplFactory extends EtcdService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final EtcdImplLogger log;

    private DebugLogging debug;

    private final List<Binding> bindings;

    private final List<Binding> advertises;

    private final BindingFactory bindingFactory;

    private String member;

    private final TlsFactory tlsFactory;

    private Tls tls;

    @Inject
    private Map<String, AuthenticationFactory> authenticationFactories;

    private final List<Authentication> authentications;

    @Inject
    private PeerImplFactory peerFactory;

    private Peer peer;

    @Inject
    private ClientImplFactory clientFactory;

    private Client client;

    @Inject
    private ProxyImplFactory proxyFactory;

    private Proxy proxy;

    @Inject
    private GatewayImplFactory gatewayFactory;

    private Gateway gateway;

    private SshHost checkHost;

    @Inject
    EtcdImpl(EtcdImplLogger log, HostServicePropertiesService propertiesService,
            BindingFactory bindingFactory, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.bindings = new ArrayList<>();
        this.advertises = new ArrayList<>();
        this.bindingFactory = bindingFactory;
        this.tls = tlsFactory.create();
        this.tlsFactory = tlsFactory;
        this.authentications = new ArrayList<>();
        this.checkHost = null;
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugLoggingService debugService) {
        this.debug = debugService.create();
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
     * debug "error", level: 1
     * </pre>
     */
    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    /**
     * <pre>
     * debug name: "error", level: 1
     * </pre>
     */
    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<>(args);
        invokeMethod(debug, "debug", arguments);
    }

    /**
     * <pre>
     * debug << [name: "error", level: 1]
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
    }

    /**
     * <pre>
     * bind "http://10.0.1.10:2379"
     * </pre>
     */
    public void bind(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        bind(a);
    }

    /**
     * <pre>
     * bind network: "enp0s8:1", "http://10.10.10.7:22379"
     * </pre>
     */
    public void bind(Map<String, Object> args, String address) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("address", address);
        bind(a);
    }

    /**
     * <pre>
     * bind address: "http://10.0.1.10:2379"
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        Binding binding = bindingFactory.create(a);
        log.bindingAdded(this, binding);
        bindings.add(binding);
    }

    /**
     * <pre>
     * advertise "http://10.0.1.10:2380"
     * </pre>
     */
    public void advertise(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        advertise(a);
    }

    /**
     * <pre>
     * advertise address: "http://10.0.1.10:2380"
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
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
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
     * peer state: "new", advertise: "https://10.0.1.10:2380", listen: "https://10.0.1.10:2380", token: "etcd-cluster-1"
     * </pre>
     */
    public Peer peer(Map<String, Object> args) {
        Peer peer = peerFactory.create(args);
        this.peer = peer;
        log.peerSet(this, peer);
        return peer;
    }

    /**
     * <pre>
     * client ca: "ca.pem", cert: "cert.pem", key: "key.pem"
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
     * proxy namespace: "my-prefix/", endpoints: "https://etcd-0:2379"
     * </pre>
     */
    public Proxy proxy(Map<String, Object> args) {
        Proxy proxy = proxyFactory.create(args);
        this.proxy = proxy;
        log.proxySet(this, proxy);
        return proxy;
    }

    /**
     * <pre>
     * gateway endpoints: "https://etcd-0:2379"
     * </pre>
     */
    public Gateway gateway(Map<String, Object> args) {
        Gateway gateway = gatewayFactory.create(args);
        this.gateway = gateway;
        log.gatewaySet(this, gateway);
        return gateway;
    }

    /**
     * <pre>
     * gateway()
     * </pre>
     */
    public Gateway gateway() {
        return gateway(new HashMap<String, Object>());
    }

    /**
     * <pre>
     * check on: targets.default[0]
     * </pre>
     */
    public void check(Map<String, Object> args) {
        Object v = args.get("on");
        assertThat("check on=null", v, notNullValue());
        this.checkHost = (SshHost) v;
    }

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
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
        return "etcd";
    }

    public void setMemberName(String member) {
        this.member = member;
        log.memberNameSet(this, member);
    }

    @Override
    public String getMemberName() {
        return member;
    }

    @Override
    public List<Binding> getBindings() {
        return bindings;
    }

    @Override
    public List<Binding> getAdvertises() {
        return advertises;
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public List<Authentication> getAuthentications() {
        return authentications;
    }

    @Override
    public Peer getPeer() {
        return peer;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public Gateway getGateway() {
        return gateway;
    }

    public void setCheckHost(SshHost host) {
        this.checkHost = host;
    }

    @Override
    public SshHost getCheckHost() {
        return checkHost;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseMember(args);
        parseCheck(args);
    }

    private void parseCheck(Map<String, Object> args) {
        Object v = args.get("check");
        if (v != null) {
            setCheckHost((SshHost) v);
        }
    }

    private void parseMember(Map<String, Object> args) {
        Object v = args.get("member");
        if (v != null) {
            setMemberName(v.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

}
