package com.anrisoftware.sscontrol.services.internal.cluster;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.services.internal.targets.AbstractTargetsImpl;
import com.anrisoftware.sscontrol.types.external.cluster.Cluster;
import com.anrisoftware.sscontrol.types.external.cluster.ClusterHost;
import com.anrisoftware.sscontrol.types.external.cluster.Clusters;
import com.anrisoftware.sscontrol.types.external.cluster.ClustersService;

/**
 * Cluster targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClustersImpl extends AbstractTargetsImpl<ClusterHost, Cluster>
        implements Clusters {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClustersImplFactory extends ClustersService {

    }

    @Inject
    ClustersImpl() {
    }
}
