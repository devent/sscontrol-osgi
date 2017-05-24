package com.anrisoftware.sscontrol.icinga.service.external;

/**
 * Database credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Database {

    String getHost();

    String getUser();

    String getPassword();

    String getDatabase();

    String getAdminUser();

    String getAdminPassword();

}
