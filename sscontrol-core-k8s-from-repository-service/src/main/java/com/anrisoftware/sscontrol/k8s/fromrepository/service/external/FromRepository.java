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
package com.anrisoftware.sscontrol.k8s.fromrepository.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterService;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * From repository cluster service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface FromRepository extends ClusterService {

    /**
     * Returns template variables.
     */
    Map<String, Object> getVars();

    void addRepos(List<RepoHost> list);

    RepoHost getRepo();

    List<RepoHost> getRepos();

    RegistryHost getRegistry();

    void addRegistries(List<RegistryHost> list);

    List<RegistryHost> getRegistries();

    /**
     * Returns the manifests destination directory or <code>null</code>.
     */
    String getDestination();

    /**
     * Returns true if it should just output the generated manifests but not run
     * anything.
     */
    boolean getDryrun();

    /**
     * Returns the custom resource definitions that should be ignored.
     */
    List<Crd> getCrds();
}
