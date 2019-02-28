package com.anrisoftware.sscontrol.haproxy.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.haproxy.service.external.Backend;
import com.google.inject.assistedinject.Assisted;

/**
 * Proxy backend.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class BackendImpl extends AbstractTarget implements Backend {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BackendImplFactory {

        Backend create(Map<String, Object> args);
    }

    @Inject
    BackendImpl(@Assisted Map<String, Object> args) {
        super(args);
    }

}
