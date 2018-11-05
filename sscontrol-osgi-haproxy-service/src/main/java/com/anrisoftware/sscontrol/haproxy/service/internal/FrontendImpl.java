package com.anrisoftware.sscontrol.haproxy.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;
import com.google.inject.assistedinject.Assisted;

/**
 * Proxy frontend.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FrontendImpl extends AbstractTarget implements Frontend {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FrontendImplFactory {

        Frontend create(Map<String, Object> args);
    }

    @Inject
    FrontendImpl(@Assisted Map<String, Object> args) {
        super(args);
    }

}
