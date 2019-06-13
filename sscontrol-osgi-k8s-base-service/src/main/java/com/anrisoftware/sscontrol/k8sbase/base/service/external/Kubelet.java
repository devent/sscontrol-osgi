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
package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Kubelet client.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public interface Kubelet {

    /**
     * Returns the address to bind kubelet to.
     *
     * @return {@link String}
     */
    String getAddress();

    /**
     * Returns the port to bind kubelet to.
     *
     * @return {@link Integer}
     */
    Integer getPort();

    /**
     * Returns the read-only port.
     *
     * @return {@link Integer}
     */
    Integer getReadOnlyPort();

    /**
     * Kubelet TLS.
     */
    Tls getTls();

    /**
     * Kubelet client TLS.
     */
    Tls getClient();

    /**
     * List of the preferred NodeAddressTypes.
     */
    List<String> getPreferredAddressTypes();

}
