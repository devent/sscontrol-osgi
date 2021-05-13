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
package com.anrisoftware.sscontrol.crio.script.debian.debian_10

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * CRI-O 1.20 Debian 10.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Crio_1_20_Debian_10 extends ScriptBase {

    @Inject
    Crio_1_20_Debian_10_Properties propertiesProvider

    Upstream_Crio_Debian_10 upstreamCrio

    Crio_Crio_1_20_Debian_10 debianCrio

    @Override
    def run() {
        debianCrio.configureK8s()
        upstreamCrio.installCrio()
        debianCrio.configureCgroupDriver()
        debianCrio.enableService()
    }

    @Inject
    void setUpstreamCrioDebianFactory(Upstream_Crio_Debian_10_Factory factory) {
        this.upstreamCrio = factory.create scriptsRepository, service, target, threads, scriptEnv
    }

    @Inject
    void setCrioDebianFactory(Crio_Crio_1_20_Debian_10_Factory factory) {
        this.debianCrio = factory.create scriptsRepository, service, target, threads, scriptEnv
    }

    @Override
    String getSystemName() {
        Crio_1_20_Debian_10_Service.SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        Crio_1_20_Debian_10_Service.SYSTEM_VERSION
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
