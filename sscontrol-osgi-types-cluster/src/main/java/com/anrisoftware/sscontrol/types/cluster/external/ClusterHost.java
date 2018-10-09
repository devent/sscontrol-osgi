package com.anrisoftware.sscontrol.types.cluster.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * Cluster target host.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterHost extends TargetHost {

    List<Credentials> getCredentials();

    String getClusterName();

    URI getUrl() throws URISyntaxException;
}
