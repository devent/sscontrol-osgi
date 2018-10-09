package com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Git-Repo</i> properties provider from
 * {@code "/git_repo_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GitRepoDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = GitRepoDebianProperties.class.getResource("/git_repo_debian_9.properties")

    GitRepoDebianProperties() {
        super(GitRepoDebianProperties.class, RESOURCE)
    }
}
