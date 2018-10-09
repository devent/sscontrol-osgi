package com.anrisoftware.sscontrol.k8s.fromhelm.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * From Helm service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface FromHelm extends HostService {

    /**
     * Returns the configuration.
     */
    Object getConfigYaml();

    /**
     * Returns the chart to install.
     *
     * @return the {@link String} chart or <code>null</code>.
     */
    String getChart();

    /**
	 * Returns the chart version to install.
	 *
	 * @return the {@link String} chart version or <code>null</code>.
	 */
	String getVersion();

	/**
	 * Returns the Helm release.
	 *
	 * @return the {@link Release}.
	 */
	Release getRelease();

	/**
	 * Adds the specified source repositories.
	 *
	 * @param list
	 *            the {@link List} of {@link RepoHost}.
	 */
    void addRepos(List<RepoHost> list);

    /**
     * Returns the repository host or <code>null</code>.
     *
     * @return {@link RepoHost} or <code>null</code>.
     */
    RepoHost getRepo();

    /**
     * Returns if we use repository instead of a chart.
     *
     * @return <code>true</code> if a repository is set.
     */
    boolean getUseRepo();

    /**
     * Returns the repository hosts.
     *
     * @return {@link List} of {@link RepoHost}.
     */
    List<RepoHost> getRepos();

    /**
     * Returns true if it should just output the generated manifests but not run
     * anything.
     */
    boolean getDryrun();
}
