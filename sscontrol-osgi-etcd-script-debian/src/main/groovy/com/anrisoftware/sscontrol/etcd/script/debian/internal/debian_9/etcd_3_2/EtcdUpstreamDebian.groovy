/*-
 * #%L
 * sscontrol-osgi - etcd-script-debian
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.script.upstream.external.Etcd_3_x_Upstream

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.2 service from the upstream sources
 * for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdUpstreamDebian extends Etcd_3_x_Upstream {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        installEtcd()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
