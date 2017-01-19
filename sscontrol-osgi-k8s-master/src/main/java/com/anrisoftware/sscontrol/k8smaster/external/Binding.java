package com.anrisoftware.sscontrol.k8smaster.external;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Binding {

    String getInsecureAddress();

    String getSecureAddress();

    Integer getPort();
}
