package com.anrisoftware.sscontrol.k8sbase.base.external;

/**
 * Kubernetes label.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Label {

    String getKey();

    String getValue();
}
