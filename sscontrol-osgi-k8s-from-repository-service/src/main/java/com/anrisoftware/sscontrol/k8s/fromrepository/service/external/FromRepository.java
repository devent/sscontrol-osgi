package com.anrisoftware.sscontrol.k8s.fromrepository.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterService;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * From repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface FromRepository extends ClusterService {

    /**
     * Returns template variables.
     */
    Map<String, Object> getVars();

    void addRepos(List<RepoHost> list);

    RepoHost getRepo();

    List<RepoHost> getRepos();

    RegistryHost getRegistry();

    void addRegistries(List<RegistryHost> list);

    List<RegistryHost> getRegistries();

    /**
     * Returns the manifests destination directory or <code>null</code>.
     */
    String getDestination();

    /**
     * Returns true if it should just output the generated manifests but not run
     * anything.
     */
    boolean getDryrun();
}
