package com.anrisoftware.sscontrol.zimbra.service.external;

/**
 * Zimbra domain.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Domain {

    /**
     * Returns the administrator email address.
     */
    String getName();

    /**
     * Returns the administrator email address.
     */
    String getEmail();
}
