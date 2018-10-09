package com.anrisoftware.sscontrol.services.internal.repo;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.services.internal.repo.ReposImpl.ReposImplFactory;
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule;
import com.anrisoftware.sscontrol.types.repo.external.Repos;
import com.anrisoftware.sscontrol.types.repo.external.ReposService;

/**
 * Creates the code repositories.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(ReposService.class)
public class ReposServiceImpl implements ReposService {

    @Inject
    private ReposImplFactory targetsFactory;

    private Repos targets;

    @Override
    public synchronized Repos create() {
        if (targets == null) {
            this.targets = targetsFactory.create();
        }
        return targets;
    }

    @Activate
    protected void start() {
        createInjector(new TargetsModule()).injectMembers(this);
    }
}
