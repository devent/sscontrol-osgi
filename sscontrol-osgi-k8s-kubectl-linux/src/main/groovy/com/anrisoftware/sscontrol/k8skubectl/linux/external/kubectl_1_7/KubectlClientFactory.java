package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface KubectlClientFactory {

    KubectlClient create(HostServiceScript script, ClusterHost cluster);
}

