/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromhelm.service.internal;

import static com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImplLogger.m.registriesAdded;
import static com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImplLogger.m.reposAdded;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.Release;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * Logging for {@link FromHelmImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class FromHelmImplLogger extends AbstractLogger {

	enum m {

		clustersAdded("Clusters {} added to {}"),

		reposAdded("Repositories {} added to {}"),

		registriesAdded("Repositories {} added to {}"),

		destinationSet("Manifests destination {} set for {}"),

		dryrunSet("Dry-run {} set for {}"),

		yamlConfigSet("Yaml config set for {}: {}"),

		chartSet("Chart {} set for {}"),

		versionSet("Version {} set for {}"),

		releaseSet("Release {} set for {}");

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
	 * Sets the context of the logger to {@link FromHelmImpl}.
	 */
	FromHelmImplLogger() {
		super(FromHelmImpl.class);
	}

	void clustersAdded(FromHelmImpl from, List<ClusterHost> list) {
		debug(clustersAdded, list, from);
	}

	void reposAdded(FromHelmImpl from, List<RepoHost> list) {
		debug(reposAdded, list, from);
	}

	void registriesAdded(FromHelmImpl from, List<RegistryHost> list) {
		debug(registriesAdded, list, from);
	}

	void destinationSet(FromHelmImpl from, String dest) {
		debug(m.destinationSet, dest, from);
	}

	void dryrunSet(FromHelmImpl from, boolean dryrun) {
		debug(m.dryrunSet, dryrun, from);
	}

	void yamlConfigSet(FromHelmImpl from, Object config) {
		debug(m.yamlConfigSet, from, config);
	}

	void chartSet(FromHelmImpl from, String chart) {
		debug(m.chartSet, chart, from);
	}

	void versionSet(FromHelmImpl from, String version) {
		debug(m.versionSet, version, from);
	}

	void releaseSet(FromHelmImpl from, Release release) {
		debug(m.releaseSet, release, from);
	}
}
