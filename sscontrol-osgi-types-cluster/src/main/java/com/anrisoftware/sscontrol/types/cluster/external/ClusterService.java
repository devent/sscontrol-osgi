/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.types.cluster.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Cluster service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterService extends HostService {

    /**
     * Returns the host that can be used to have access to the cluster via
     * kubectl.
     */
    ClusterHost getClusterHost();

    /**
     * Returns the hosts that can be used to have access to the clusters via
     * kubectl.
     */
    List<ClusterHost> getClusterHosts();

}
