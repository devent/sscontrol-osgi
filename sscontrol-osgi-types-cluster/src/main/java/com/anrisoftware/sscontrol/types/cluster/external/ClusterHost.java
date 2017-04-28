package com.anrisoftware.sscontrol.types.cluster.external;

import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * Cluster target host.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterHost extends TargetHost {

    String getProto();

}
