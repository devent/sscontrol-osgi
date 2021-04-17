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

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.FromHelm;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.Release;
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.ReleaseImpl.ReleaseImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;
import com.google.inject.assistedinject.Assisted;

/**
 * From Helm service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FromHelmImpl implements FromHelm {

	/**
	 *
	 *
	 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
	 * @version 1.0
	 */
	public interface FromHelmImplFactory extends HostServiceService {

	}

	private final FromHelmImplLogger log;

	private HostServiceProperties serviceProperties;

	private final List<TargetHost> targets;

	private final List<RepoHost> repos;

	private Object configYaml;

	private boolean dryrun;

	private String chart;

	private String version;

	private Release release;

	private transient ReleaseImplFactory releaseFactory;

	@Inject
	FromHelmImpl(FromHelmImplLogger log, HostServicePropertiesService propertiesService,
			ReleaseImplFactory releaseFactory, @Assisted Map<String, Object> args) {
		this.log = log;
		this.serviceProperties = propertiesService.create();
		this.targets = new ArrayList<>();
		this.repos = new ArrayList<>();
		this.dryrun = false;
		this.releaseFactory = releaseFactory;
		this.release = releaseFactory.create();
		parseArgs(args);
	}

	/**
	 * <pre>
	 * property << 'name=value'
	 * </pre>
	 */
	public List<String> getProperty() {
		return stringListStatement(new ListProperty() {

			@Override
			public void add(String property) {
				serviceProperties.addProperty(property);
			}
		});
	}

	@Override
	public Object getConfigYaml() {
		return configYaml;
	}

	@Override
	public TargetHost getTarget() {
		return getTargets().get(0);
	}

	public void addTargets(List<TargetHost> list) {
		this.targets.addAll(list);
	}

	@Override
	public List<TargetHost> getTargets() {
		return Collections.unmodifiableList(targets);
	}

	@Override
	public RepoHost getRepo() {
		List<RepoHost> repos = getRepos();
		if (repos.isEmpty()) {
			return null;
		}
		return repos.get(0);
	}

	@Override
	public void addRepos(List<RepoHost> list) {
		this.repos.addAll(list);
		log.reposAdded(this, list);
	}

	@Override
	public List<RepoHost> getRepos() {
		return Collections.unmodifiableList(repos);
	}

	@Override
	public boolean getUseRepo() {
		return !getRepos().isEmpty();
	}

	public void setServiceProperties(HostServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	@Override
	public HostServiceProperties getServiceProperties() {
		return serviceProperties;
	}

	@Override
	public String getName() {
		return "from-helm";
	}

	/**
	 * <pre>
	 * config << "{mariadbUser: user0, mariadbDatabase: user0db}"
	 * </pre>
	 */
	public List<String> getConfig() {
		return stringListStatement(new ListProperty() {

			@Override
			public void add(String text) {
				parseYaml(text);
			}

		});
	}

	/**
	 * <pre>
	 * release ns: "helm-test", name: "wordpress"
	 * </pre>
	 */
	public void release(Map<String, Object> args) {
		Release release = releaseFactory.create(this.release, args);
		setRelease(release);
	}

	public void setDryrun(boolean dryrun) {
		this.dryrun = dryrun;
		log.dryrunSet(this, dryrun);
	}

	@Override
	public boolean getDryrun() {
		return dryrun;
	}

	private void setChart(String chart) {
		this.chart = chart;
		log.chartSet(this, chart);
	}

	@Override
	public String getChart() {
		return chart;
	}

	public void setVersion(String version) {
		this.version = version;
		log.versionSet(this, version);
	}

	@Override
	public String getVersion() {
		return version;
	}

	public void setRelease(Release release) {
		this.release = release;
		log.releaseSet(this, release);
	}

	@Override
	public Release getRelease() {
		return release;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).append("targets", getTargets())
				.append("repos", getRepos()).toString();
	}

	private void parseArgs(Map<String, Object> args) {
		parseTargets(args);
		parseRepos(args);
		parseDryrun(args);
		parseChart(args);
		parseVersion(args);
		parseNs(args);
		parseName(args);
	}

	private void parseName(Map<String, Object> args) {
		Object v = args.get("name");
		if (v != null) {
			Release release = releaseFactory.create(this.release, args);
			setRelease(release);
		}
	}

	private void parseNs(Map<String, Object> args) {
		Object v = args.get("ns");
		if (v != null) {
			Release release = releaseFactory.create(this.release, args);
			setRelease(release);
		}
	}

	private void parseVersion(Map<String, Object> args) {
		Object v = args.get("version");
		if (v != null) {
			setVersion(v.toString());
		}
	}

	private void parseChart(Map<String, Object> args) {
		Object v = args.get("chart");
		if (v != null) {
			setChart(v.toString());
		}
	}

	private void parseDryrun(Map<String, Object> args) {
		Object v = args.get("dryrun");
		if (v != null) {
			setDryrun((boolean) v);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseTargets(Map<String, Object> args) {
		Object v = args.get("targets");
		assertThat("targets=null", v, notNullValue());
		addTargets((List<TargetHost>) v);
	}

	@SuppressWarnings("unchecked")
	private void parseRepos(Map<String, Object> args) {
		Object v = args.get("repos");
		assertThat("repos=null", v, notNullValue());
		addRepos((List<RepoHost>) v);
	}

	private void parseYaml(String text) {
		StringBuilder b = new StringBuilder();
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);
		if (configYaml != null) {
			String output = yaml.dump(configYaml);
			b.append(output);
		}
		b.append(text);
		this.configYaml = yaml.load(b.toString());
		log.yamlConfigSet(this, configYaml);
	}

}
