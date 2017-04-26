package com.anrisoftware.sscontrol.k8sbase.base.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.host.HostService;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;

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

    void addTargets(List<SshHost> list);

    void setContainerRuntime(String runtime);

    Kubelet kubelet(Map<String, Object> args);

}
