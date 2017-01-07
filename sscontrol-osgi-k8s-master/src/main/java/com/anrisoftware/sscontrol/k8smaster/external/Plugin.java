package com.anrisoftware.sscontrol.k8smaster.external;

import java.util.Map;

/**
 * K8s plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Plugin {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface PluginFactory {

        Plugin create();

        Plugin create(Map<String, Object> args);

    }

    String getName();
}
