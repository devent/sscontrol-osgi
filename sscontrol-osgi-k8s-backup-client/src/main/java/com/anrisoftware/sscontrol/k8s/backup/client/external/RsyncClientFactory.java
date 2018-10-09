package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface RsyncClientFactory {

    RsyncClient create(ScriptBase script, Service service, ClusterHost cluster,
            Client client, Destination destination);
}
