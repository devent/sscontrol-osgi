package com.anrisoftware.sscontrol.types.ssh.external;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface TargetsAddressListFactory {

    TargetsAddressList create(HostService service, HostServices repo,
            String targetsPropertyName, Object parent);
}
