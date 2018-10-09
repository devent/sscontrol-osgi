package com.anrisoftware.sscontrol.docker.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.docker.service.external.DockerService;
import com.anrisoftware.sscontrol.docker.service.internal.DockerImpl.DockerImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * <i>Docker</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class DockerServiceImpl implements DockerService {

    @Inject
    private DockerImplFactory serviceFactory;

    @Override
    public String getName() {
        return "docker";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return serviceFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new DockerModule()).injectMembers(this);
    }
}
