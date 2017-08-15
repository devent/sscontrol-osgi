package com.anrisoftware.sscontrol.k8s.backup.client.external;

/**
 * Backup source.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Source {

    String getSource();

    String getTarget();

    String getChown();

}
