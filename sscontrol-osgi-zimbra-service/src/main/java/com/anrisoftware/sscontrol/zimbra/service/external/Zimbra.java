package com.anrisoftware.sscontrol.zimbra.service.external;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Zimbra service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Zimbra extends HostService {

    String getVersion();

    Domain getDomain();
}
