package com.anrisoftware.sscontrol.k8sbase.base.service.external;

/**
 * <i>Canal</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface CanalPlugin extends Plugin {

    /**
     * Returns the interface used by canal for host <-> host communication.
     *
     * @return {@link String}
     */
    String getIface();
}
