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
package com.anrisoftware.sscontrol.k8smaster.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostService;

/**
 * <i>K8s-Master</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sMaster extends HostService {

    DebugLogging getDebugLogging();

    Cluster getCluster();

    Map<String, Plugin> getPlugins();

    Boolean isAllowPrivileged();

    Tls getTls();

    List<Authentication> getAuthentications();

    List<Authorization> getAuthorizations();

    List<String> getAdmissions();

    Kubelet getKubelet();

    Binding getBinding();
}
