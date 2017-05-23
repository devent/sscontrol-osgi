package com.anrisoftware.sscontrol.icinga.service.external;

/**
 * ido-mysql plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface IdoMysqlPlugin extends Plugin {

    Database getDatabase();
}
