package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker</i> properties provider from
 * {@code "/docker_registry_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DockerRegistryLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = DockerRegistryLinuxProperties.class.getResource("/docker_registry_debian_8.properties")

    DockerRegistryLinuxProperties() {
        super(DockerRegistryLinuxProperties.class, RESOURCE)
    }
}
