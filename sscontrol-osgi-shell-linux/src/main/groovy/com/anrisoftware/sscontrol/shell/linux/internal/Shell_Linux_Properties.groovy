package com.anrisoftware.sscontrol.shell.linux.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * Shell GNU/Linux properties provider from
 * {@code "/shell_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Shell_Linux_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Shell_Linux_Properties.class.getResource("/shell_linux_0.properties");

    Shell_Linux_Properties() {
        super(Shell_Linux_Properties.class, RESOURCE);
    }
}
