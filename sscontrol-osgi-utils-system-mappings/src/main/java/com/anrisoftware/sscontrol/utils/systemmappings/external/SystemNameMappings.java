package com.anrisoftware.sscontrol.utils.systemmappings.external;

/**
 * Maps system names.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface SystemNameMappings {

    /**
     * Returns the system to the name.
     *
     * @param system
     *            the name like debian, centos, windows.
     * @return the system like linux, macos, windows.
     */
    String getMapping(String system);

}
