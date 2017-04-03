package com.anrisoftware.sscontrol.types.external;

import java.util.List;

/**
 * Target host script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface TargetHostService<HostType extends TargetHost>
        extends HostService {

    /**
     * Returns the group the target belong to.
     */
    String getGroup();

    /**
     * Returns the hosts.
     */
    List<HostType> getHosts();

}
