package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDockerPreScriptService;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerPreScriptImpl.K8sMasterPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * <i>Etcd</i> pre-script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = FlannelDockerPreScriptService.class)
public class FlannelDockerPreScriptServiceImpl implements FlannelDockerPreScriptService {

    @Inject
    private K8sMasterPreScriptImplFactory preScriptFactory;

    @Override
    public PreHost create() {
        return preScriptFactory.create();
    }

    @Activate
    protected void start() {
        Guice.createInjector(new FlannelDockerPreModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }
}
