/*-
 * #%L
 * sscontrol-osgi - k8s-master-script-debian
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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUfwLinux

import groovy.util.logging.Slf4j

/**
 * Ufw.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterUfwDebian extends AbstractK8sUfwLinux {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
