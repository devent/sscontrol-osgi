package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * From repository service for Kubernetes properties provider from
 * {@code "/from_repository_1_8_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FromRepositoryLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = FromRepositoryLinuxProperties.class.getResource("/from_repository_1_8_linux.properties")

    FromRepositoryLinuxProperties() {
        super(FromRepositoryLinuxProperties.class, RESOURCE)
    }
}
