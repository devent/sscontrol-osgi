/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterService;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * Glusterfs-Heketi service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface GlusterfsHeketi extends ClusterService {

    /**
     * Returns the namespace to use for creating resources.
     */
    String getNamespace();

    /**
     * Returns the label name for which nodes glusterfs should be deployed. The
     * node affinity will be set to <code>storagenode=name</core>
     */
    String getLabelName();

    /**
     * Returns variables to use in the manifests templates.
     */
    Map<String, Object> getVars();

    /**
     * Returns the secret for heketi administrator user. Heketi admin has access
     * to all the APIs.
     */
    Admin getAdmin();

    /**
     * Secret for heketi general user. Heketi user has access to only Volume
     * APIs.
     */
    User getUser();

    /**
     * Returns the cluster topology.
     */
    Map<String, Object> getTopology();

    /**
     * Returns the glusterfs nodes.
     */
    List<Object> getNodes();

    /**
     * Returns the maximum allowed brick size in GB.
     */
    Integer getMaxBrickSizeGb();

    /**
     * Returns the minimum allowed brick size in GB.
     */
    Integer getMinBrickSizeGb();

    RepoHost getRepo();

    void addRepos(List<RepoHost> list);

    List<RepoHost> getRepos();

    Storage getStorage();

    /**
     * Returns the address of heketi service for the storage class.
     */
    String getServiceAddress();

}
