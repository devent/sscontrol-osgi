package com.anrisoftware.sscontrol.k8sbase.base.service.external;

/**
 * Node taint.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Taint {

    String getKey();

    String getValue();

    String getEffect();
}
