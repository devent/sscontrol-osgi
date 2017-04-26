package com.anrisoftware.sscontrol.types.external.cluster;

import com.anrisoftware.sscontrol.types.external.ssh.TargetHost;

/**
 * Cluster target host.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterHost extends TargetHost {

    String getProto();

}
