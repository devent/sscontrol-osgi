package com.anrisoftware.sscontrol.services.internal;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.external.ssh.Ssh;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
import com.anrisoftware.sscontrol.types.external.ssh.Targets;
import com.anrisoftware.sscontrol.types.external.ssh.TargetsService;

/**
 * Ssh host targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsImpl extends AbstractTargetsImpl<SshHost, Ssh>
        implements Targets {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TargetsImplFactory extends TargetsService {

    }

    @Inject
    TargetsImpl() {
    }
}
