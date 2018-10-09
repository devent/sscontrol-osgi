package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_8

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Backup service for Kubernetes properties provider from
 * {@code "/backup_1_8_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BackupLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = BackupLinuxProperties.class.getResource("/backup_1_8_linux.properties")

    BackupLinuxProperties() {
        super(BackupLinuxProperties.class, RESOURCE)
    }
}
