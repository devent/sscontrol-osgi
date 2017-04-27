package com.anrisoftware.sscontrol.repo.git.service.external;

import com.anrisoftware.sscontrol.types.external.repo.RepoHost;

/**
 * <i>Git</i> code repository host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface GitRepoHost extends RepoHost {

    GitRepo getRepo();

}
