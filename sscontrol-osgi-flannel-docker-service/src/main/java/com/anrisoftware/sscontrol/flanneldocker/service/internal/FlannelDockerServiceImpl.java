package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDockerService;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImpl.FlannelDockerImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * <i>Etcd</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class FlannelDockerServiceImpl implements FlannelDockerService {

    @Inject
    private FlannelDockerImplFactory serviceFactory;

    @Override
    public String getName() {
        return "flannel-docker";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return serviceFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new FlannelDockerModule()).injectMembers(this);
    }
}
