package com.anrisoftware.sscontrol.rkt.service.external;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Rkt service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Rkt extends HostService {

    String getVersion();
}
