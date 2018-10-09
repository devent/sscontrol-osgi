package com.anrisoftware.sscontrol.k8s.backup.client.internal;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory;
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient;
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClientFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class BackupClientModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(Deployment.class, DeploymentImpl.class)
                .build(DeploymentFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RsyncClient.class, RsyncClientImpl.class)
                .build(RsyncClientFactory.class));
    }

}
