package com.anrisoftware.sscontrol.utils.systemd.external;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;

/**
 * Debian 8 utilities factory.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface SystemdUtilsFactory {

    SystemdUtils create(HostServiceScript script);
}
