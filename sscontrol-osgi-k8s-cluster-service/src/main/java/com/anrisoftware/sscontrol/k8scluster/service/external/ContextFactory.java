package com.anrisoftware.sscontrol.k8scluster.service.external;

import java.util.Map;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface ContextFactory {

    Context create(Map<String, Object> args);
}

