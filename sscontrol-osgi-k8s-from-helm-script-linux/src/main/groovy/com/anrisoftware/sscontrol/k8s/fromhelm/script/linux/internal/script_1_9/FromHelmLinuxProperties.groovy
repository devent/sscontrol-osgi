package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * From repository service for Kubernetes properties provider from
 * {@code "/from_helm_1_9_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FromHelmLinuxProperties extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = FromHelmLinuxProperties.class.getResource("/from_helm_1_9_linux.properties")

	FromHelmLinuxProperties() {
		super(FromHelmLinuxProperties.class, RESOURCE)
	}
}
