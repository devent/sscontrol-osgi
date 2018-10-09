package com.anrisoftware.sscontrol.flanneldocker.service.external;

import java.util.Map;

/**
 * <i>Flannel-Docker</i> backend.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Backend {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BackendFactory {

        Backend create();

        Backend create(Map<String, Object> args);

    }

    String getType();
}
