package com.anrisoftware.sscontrol.k8s.restore.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService;

/**
 * Restore service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Restore extends ClusterService {

    /**
     * Returns the service to backup.
     */
    Service getService();

    /**
     * Returns the backup origin.
     */
    Destination getOrigin();

    /**
     * Returns the backup client.
     */
    Client getClient();

    /**
     * Returns the backup targets.
     */
    List<Source> getSources();

	/**
	 * Returns true if it should just simulate the restoration and actually not
	 * transfer any files.
	 */
	boolean getDryrun();
}
