package com.anrisoftware.sscontrol.utils.ufw.linux.external;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;

/**
 * Ufw utilities factory.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface UfwLinuxUtilsFactory {

    UfwUtils create(HostServiceScript script);
}
