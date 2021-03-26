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
package com.anrisoftware.sscontrol.k8snode.service.internal;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Label;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImpl.K8sImplFactory;
import com.anrisoftware.sscontrol.k8snode.service.external.K8sNode;
import com.anrisoftware.sscontrol.k8snode.service.external.K8sNodeService;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Node</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sNodeImpl implements K8sNode {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface K8sNodeImplFactory extends K8sNodeService {

    }

    private final K8s k8s;

    @Inject
    K8sNodeImpl(K8sImplFactory k8sFactory, @Assisted Map<String, Object> args) {
        this.k8s = (K8s) k8sFactory.create(args);
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

    @Override
    public DebugLogging getDebugLogging() {
        return k8s.getDebugLogging();
    }

    @Override
    public void addTargets(List<TargetHost> list) {
        k8s.addTargets(list);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return k8s.getTargets();
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return k8s.getServiceProperties();
    }

    @Override
    public String getName() {
        return "k8s-node";
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
    public Tls getTls() {
        return k8s.getTls();
    }

    @Override
    public Boolean isAllowPrivileged() {
        return k8s.isAllowPrivileged();
    }

    @Override
    public Kubelet getKubelet() {
        return k8s.getKubelet();
    }

    @Override
    public void setContainerRuntime(String runtime) {
        k8s.setContainerRuntime(runtime);
    }

    @Override
    public String getContainerRuntime() {
        return k8s.getContainerRuntime();
    }

    @Override
    public Map<String, Label> getLabels() {
        return k8s.getLabels();
    }

    @Override
    public void label(Map<String, Object> args) {
        k8s.label(args);
    }

    @Override
    public List<String> getLabel() {
        return k8s.getLabel();
    }

    @Override
    public Map<String, Taint> getTaints() {
        return k8s.getTaints();
    }

    @Override
    public void taint(Map<String, Object> args) {
        k8s.taint(args);
    }

    @Override
    public List<String> getTaint() {
        return k8s.getTaint();
    }

    @Override
    public ClusterHost getClusterHost() {
        return k8s.getClusterHost();
    }

    @Override
    public List<ClusterHost> getClusterHosts() {
        return k8s.getClusterHosts();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets()).toString();
    }

}
