package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class K8sClusterLinuxServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HostServiceScriptService.class)
                .annotatedWith(Names.named("cluster-service"))
                .to(K8sClusterLinuxService.class);
    }

}
