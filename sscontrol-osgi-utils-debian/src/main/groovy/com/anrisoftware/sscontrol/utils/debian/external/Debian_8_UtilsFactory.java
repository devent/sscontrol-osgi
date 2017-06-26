package com.anrisoftware.sscontrol.utils.debian.external;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;

/**
 * Debian 8 utilities factory.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Debian_8_UtilsFactory {

    DebianUtils create(HostServiceScript script);
}
