package com.anrisoftware.sscontrol.ssh.script.linux.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Ssh GNU/Linux</i> properties provider from
 * {@code "/ssh_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SshLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = SshLinuxProperties.class.getResource("/ssh_linux_0.properties");

    SshLinuxProperties() {
        super(SshLinuxProperties.class, RESOURCE);
    }
}
