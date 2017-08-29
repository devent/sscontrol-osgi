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
package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;

/**
 * <i>K8s</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8s extends HostService {

    Map<String, Plugin> getPlugins();

    DebugLogging getDebugLogging();

    Cluster getCluster();

    Boolean isAllowPrivileged();

    Kubelet getKubelet();

    Tls getTls();

    String getContainerRuntime();

    List<String> getProperty();

    void target(Map<String, Object> args);

    void debug(Map<String, Object> args, String name);

    void debug(Map<String, Object> args);

    List<Object> getDebug();

    void cluster(Map<String, Object> args);

    Plugin plugin(String name);

    Plugin plugin(Map<String, Object> args, String name);

    Plugin plugin(Map<String, Object> args);

    void privileged(boolean allow);

    void tls(Map<String, Object> args);

    void addTargets(List<TargetHost> list);

    void setContainerRuntime(String runtime);

    Kubelet kubelet(Map<String, Object> args);

    void addNode(Node node);

    ClusterHost getClusterHost();

    List<ClusterHost> getClusterHosts();

    /**
     * Returns the list of nodes of this Kubernetes service.
     */
    List<Node> getNodes();

    /**
     * Returns labels for the node.
     */
    Map<String, Label> getLabels();

    /**
     * <pre>
     * label key: "muellerpublic.de/some", value: "foo"
     * </pre>
     */
    void label(Map<String, Object> args);

    /**
     * <pre>
     * label << 'name=value'
     * </pre>
     */
    List<String> getLabel();

    /**
     * Returns taints for the node.
     */
    Map<String, Taint> getTaints();

    /**
     * <pre>
     * taint key: "extra", value: "foo", effect: "aaa"
     * </pre>
     */
    void taint(Map<String, Object> args);

    /**
     * <pre>
     * taint << "dedicated=mail:NoSchedule"
     * </pre>
     */
    List<String> getTaint();

}
