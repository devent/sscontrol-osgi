package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.ssh.external.SshHost;

/**
 * Information about the Kubernetes cluster.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Cluster {

    /**
     * Returns the name of the cluster.
     */
    String getName();

    /**
     * Returns the pods network CIDR, the range of IP addresses for the pod
     * network.
     */
    String getPodRange();

    /**
     * Returns the range of IP address for service VIPs.
     */
    String getServiceRange();

    /**
     * Returns the advertise address. The address can be either a string of an
     * IP address or a {@link SshHost} ssh host.
     */
    Object getAdvertiseAddress();

    /**
     * Returns the DNS domain like {@code cluster.local}
     */
    String getDnsDomain();

    /**
     * Returns the list of api servers. The list can contain a host string or
     * {@link SshHost} ssh hosts.
     */
    List<Object> getApiServers();

    /**
     * Returns the default api-server protocol.
     */
    String getProtocol();

    /**
     * Returns the default api-server port.
     */
    Integer getPort();

    /**
     * Returns the join command for the cluster.
     */
    String getJoinCommand();

}
