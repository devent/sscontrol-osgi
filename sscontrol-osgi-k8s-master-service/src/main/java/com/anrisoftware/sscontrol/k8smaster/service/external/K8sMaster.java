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
package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>K8s-Master</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sMaster extends K8s {

    List<Authentication> getAuthentications();

    List<Authorization> getAuthorizations();

    List<String> getAdmissions();

    /**
     * Returns the address and port for the api-server.
     *
     * @return {@link Binding}
     */
    Binding getBinding();

    Account getAccount();

    /**
     * Returns the list of kubernetes nodes.
     */
    List<Object> getNodes();

    /**
     * Returns the CA certificates for signing generated TLS certificates.
     */
    Tls getCa();

    /**
     * Sets the CA certificates for signing generated TLS certificates.
     *
     * <pre>
     * ca ca: "ca.pem", key: "key.pem"
     * </pre>
     */
    void ca(Map<String, Object> args);

}
