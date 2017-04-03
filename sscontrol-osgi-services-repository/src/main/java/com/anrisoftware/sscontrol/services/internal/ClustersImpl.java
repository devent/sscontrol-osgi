package com.anrisoftware.sscontrol.services.internal;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.external.Cluster;
import com.anrisoftware.sscontrol.types.external.ClusterHost;
import com.anrisoftware.sscontrol.types.external.Clusters;
import com.anrisoftware.sscontrol.types.external.ClustersService;

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
