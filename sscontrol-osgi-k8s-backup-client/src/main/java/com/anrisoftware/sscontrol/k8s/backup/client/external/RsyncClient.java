package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.util.Map;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface RsyncClient {

    void start(Map<String, Object> args);
}
