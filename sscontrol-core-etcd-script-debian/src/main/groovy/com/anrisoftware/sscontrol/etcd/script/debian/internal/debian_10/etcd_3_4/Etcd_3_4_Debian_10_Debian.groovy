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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_10_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.4 Debian 10.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Etcd_3_4_Debian_10_Debian extends ScriptBase {

    @Inject
    Etcd_3_4_Debian_10_Properties debianPropertiesProvider

    @Inject
    UpstreamEtcd_3_4_Debian_10_Factory upstreamFactory

    @Inject
    Etcd_3_4_Debian_10_DefaultsFactory etcdDefaultsFactory

    @Inject
    VirtualInterfaceEtcd_3_4_Debian_10_Factory virtualInterfaceFactory

    @Inject
    Etcd_3_4_Debian_10_CheckFactory checkFactory

    SystemdUpstreamEtcd_3_4_Debian_10_Debian etcdUpstreamSystemd

    DebianUtils debian

    ScriptBase etcdUfw

    @Inject
    void setDebian(Debian_10_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setUpstreamSystemdFactory(SystemdUpstreamEtcd_3_4_Debian_10_DebianFactory factory) {
        this.etcdUpstreamSystemd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setEtcdUfwFactory(UfwEtcd_3_4_Debian_10_Factory factory) {
        this.etcdUfw = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        etcdDefaultsFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        etcdUpstreamSystemd.stopServices()
        debian.installPackages()
        virtualInterfaceFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        etcdUfw.run()
        etcdUpstreamSystemd.run()
        checkFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
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
