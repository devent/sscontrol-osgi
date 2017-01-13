package com.anrisoftware.sscontrol.k8smaster.external;

import java.util.Map;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface AuthorizationFactory {

    Authorization create(@Assisted Map<String, Object> args);

}
