package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties

import groovy.util.logging.Slf4j

/**
 * <i>Docker</i> registry service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class DockerRegistryLinux extends DockerRegistrySudo {

    @Inject
    DockerRegistryLinuxProperties debianPropertiesProvider

    @Override
    def run() {
        super.run()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
