package com.anrisoftware.sscontrol.k8sbase.base.external;

import java.util.Map;

/**
 * Factory to create a node taint.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface TaintFactory {

    Taint create(Map<String, Object> args);

}
