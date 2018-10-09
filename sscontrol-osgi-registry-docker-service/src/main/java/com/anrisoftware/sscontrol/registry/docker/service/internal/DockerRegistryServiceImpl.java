package com.anrisoftware.sscontrol.registry.docker.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistryService;
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class DockerRegistryServiceImpl implements DockerRegistryService {

    @Inject
    private DockerRegistryImplFactory factory;

    @Override
    public String getName() {
        return "git";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return factory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new DockerRegistryModule()).injectMembers(this);
    }
}
