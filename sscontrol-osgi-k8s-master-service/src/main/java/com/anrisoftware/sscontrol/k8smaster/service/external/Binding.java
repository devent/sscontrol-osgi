package com.anrisoftware.sscontrol.k8smaster.service.external;

/**
 * Address and port for the api-server.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Binding {

    String getInsecureAddress();

    String getSecureAddress();

    Integer getInsecurePort();

    Integer getPort();
}
