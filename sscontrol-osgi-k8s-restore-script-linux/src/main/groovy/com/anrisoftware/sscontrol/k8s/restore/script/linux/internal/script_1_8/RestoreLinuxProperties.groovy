package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Restore service for Kubernetes properties provider from
 * {@code "/restore_1_8_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RestoreLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = RestoreLinuxProperties.class.getResource("/restore_1_8_linux.properties")

    RestoreLinuxProperties() {
        super(RestoreLinuxProperties.class, RESOURCE)
    }
}
