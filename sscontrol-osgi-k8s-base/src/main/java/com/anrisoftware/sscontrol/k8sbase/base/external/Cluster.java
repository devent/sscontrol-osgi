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
package com.anrisoftware.sscontrol.k8sbase.base.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.external.SshHost;

/**
 * K8s cluster.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Cluster {

    String getPodRange();

    String getServiceRange();

    String getAdvertiseAddress();

    String getDnsAddress();

    /**
     * Returns the list of api servers. The list can contain a host string or
     * {@link SshHost} ssh hosts.
     */
    List<Object> getApiServers();

    String getHostnameOverride();
}