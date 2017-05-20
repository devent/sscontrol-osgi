package com.anrisoftware.sscontrol.k8sbase.base.external;

import java.util.Map;

/**
 * Factory to create the Kubernetes node.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface NodeFactory {

    Node create(Map<String, Object> args);

}
