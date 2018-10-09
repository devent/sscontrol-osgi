package com.anrisoftware.sscontrol.types.repo.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface RepoService extends HostService {

    RepoHost getRepo();

    List<RepoHost> getRepos();

}
