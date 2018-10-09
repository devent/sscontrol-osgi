package com.anrisoftware.sscontrol.k8s.fromhelm.service.internal;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ReleaseImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ReleaseImplLogger extends AbstractLogger {

	enum m {

		namespaceSet("Namespace {} set for {}"),

		nameSet("Name {} set for {}");

		private String name;

		private m(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Sets the context of the logger to {@link ReleaseImpl}.
	 */
	ReleaseImplLogger() {
		super(ReleaseImpl.class);
	}

	void namespaceSet(ReleaseImpl release, String namespace) {
		debug(m.namespaceSet, namespace, release);
	}

	void nameSet(ReleaseImpl release, String name) {
		debug(m.nameSet, name, release);
	}
}
