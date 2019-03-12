package com.anrisoftware.sscontrol.k8s.fromrepository.service.external;

/**
 * Custom resource definition.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Crd {

    String getKind();

    String getVersion();
}
