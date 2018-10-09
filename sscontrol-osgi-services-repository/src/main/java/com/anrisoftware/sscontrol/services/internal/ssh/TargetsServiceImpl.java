package com.anrisoftware.sscontrol.services.internal.ssh;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory;
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule;
import com.anrisoftware.sscontrol.types.ssh.external.Targets;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;

/**
 * Creates the ssh host targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(TargetsService.class)
public class TargetsServiceImpl implements TargetsService {

    @Inject
    private TargetsImplFactory targetsFactory;

    private Targets targets;

    @Override
    public synchronized Targets create() {
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
