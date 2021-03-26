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
package com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.nfs.script.nfs_1_3.external.Nfs_1_3

import groovy.util.logging.Slf4j

import static com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.Nfs_1_3_Centos_7_Service.*

import javax.inject.Inject

/**
 * Nfs 1.3 on CentOS systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Nfs_1_3_Impl extends Nfs_1_3 {

    @Inject
    Nfs_1_3_Centos_7_Properties propertiesProvider
    
    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def run() {
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
