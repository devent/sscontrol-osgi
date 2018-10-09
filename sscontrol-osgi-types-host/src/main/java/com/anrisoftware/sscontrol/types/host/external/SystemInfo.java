package com.anrisoftware.sscontrol.types.host.external;

/**
 * Information about the host system.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface SystemInfo {

    /**
     * Returns system name, for example linux, windows, macos.
     */
    String getSystem();

    /**
     * Returns the distribution name, for example debian, centos, Windows.
     */
    String getName();

    /**
     * Returns the system version, for example 8 (for Debian Jessie), 7 (for
     * Windows 7).
     */
    String getVersion();
}
