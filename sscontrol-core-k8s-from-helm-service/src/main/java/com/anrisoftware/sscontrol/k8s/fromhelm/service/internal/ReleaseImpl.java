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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.Release;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Helm release.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ReleaseImpl implements Release {

	/**
	 *
	 *
	 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
	 * @version 1.0
	 */
	public interface ReleaseImplFactory {

		Release create();

		Release create(Map<String, Object> args);

		Release create(Release release, Map<String, Object> args);

	}

	private transient ReleaseImplLogger log;

	private String namespace;

	private String name;

	@AssistedInject
	ReleaseImpl(ReleaseImplLogger log) {
		this(log, new HashMap<>());
	}

	@AssistedInject
	ReleaseImpl(ReleaseImplLogger log, @Assisted Map<String, Object> args) {
		this.log = log;
		parseArgs(args);
	}

	@AssistedInject
	ReleaseImpl(ReleaseImplLogger log, @Assisted Release release, @Assisted Map<String, Object> args) {
		this.log = log;
		this.namespace = release.getNamespace();
		this.name = release.getName();
		parseArgs(args);
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
		log.namespaceSet(this, namespace);
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	public void setName(String name) {
		this.name = name;
		log.nameSet(this, name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private void parseArgs(Map<String, Object> args) {
		parseNamespace(args);
		parseName(args);
	}

	private void parseName(Map<String, Object> args) {
		Object v = args.get("name");
		if (v != null) {
			setName(v.toString());
		}
	}

	private void parseNamespace(Map<String, Object> args) {
		Object v = args.get("ns");
		if (v != null) {
			setNamespace(v.toString());
		}
	}

}
