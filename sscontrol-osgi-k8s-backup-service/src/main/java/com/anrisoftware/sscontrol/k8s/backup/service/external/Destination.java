package com.anrisoftware.sscontrol.k8s.backup.service.external;

import java.net.URI;

/**
 * Backup destination.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Destination {

    URI getDest();
}
