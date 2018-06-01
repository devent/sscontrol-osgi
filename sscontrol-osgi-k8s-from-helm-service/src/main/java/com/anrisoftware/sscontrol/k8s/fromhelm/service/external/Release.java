package com.anrisoftware.sscontrol.k8s.fromhelm.service.external;

/**
 * Helm release.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Release {

	/**
	 * Returns the namespace to install the release into.
	 */
	String getNamespace();

	/**
	 * Returns the name of the release.
	 */
	String getName();
}
