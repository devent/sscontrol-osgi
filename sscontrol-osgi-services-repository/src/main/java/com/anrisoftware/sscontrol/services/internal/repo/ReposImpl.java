package com.anrisoftware.sscontrol.services.internal.repo;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.services.internal.targets.AbstractTargetsImpl;
import com.anrisoftware.sscontrol.types.external.repo.Repo;
import com.anrisoftware.sscontrol.types.external.repo.RepoHost;
import com.anrisoftware.sscontrol.types.external.repo.Repos;
import com.anrisoftware.sscontrol.types.external.repo.ReposService;

/**
 * Code repository targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ReposImpl extends AbstractTargetsImpl<RepoHost, Repo>
        implements Repos {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ReposImplFactory extends ReposService {

    }

    @Inject
    ReposImpl() {
    }
}
