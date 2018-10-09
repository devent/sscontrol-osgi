package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.Map;

/**
 * Factory to create a node label.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface LabelFactory {

    Label create(Map<String, Object> args);

}

