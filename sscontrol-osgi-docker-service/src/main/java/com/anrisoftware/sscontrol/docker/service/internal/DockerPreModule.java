package com.anrisoftware.sscontrol.docker.service.internal;

import com.anrisoftware.sscontrol.docker.service.internal.DockerPreScriptImpl.DockerPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Docker</i> pre-script module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DockerPreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, DockerPreScriptImpl.class)
                .build(DockerPreScriptImplFactory.class));
    }

}
