package com.anrisoftware.sscontrol.repo.git.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.repo.git.service.external.GitRepoService;
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class GitRepoServiceImpl implements GitRepoService {

    @Inject
    private GitRepoImplFactory factory;

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
        createInjector(new GitRepoModule()).injectMembers(this);
    }
}
