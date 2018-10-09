package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

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
@Component
@Service(HostServiceService.class)
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
