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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.etcd_3_2

import static com.anrisoftware.sscontrol.etcd.script.debian.internal.etcd_3_2.Etcd_3_2_Debian_9_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Etcd_3_2_Debian_9 extends ScriptBase {

    @Inject
    Etcd_3_2_Debian_9_Properties debianPropertiesProvider

    @Inject
    Etcd_3_2_Upstream_Debian_9_Factory upstreamFactory

    @Inject
    EtcdDefaultsFactory etcdDefaultsFactory

    Etcd_3_2_Upstream_Systemd_Debian_9 etcdUpstreamSystemd

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setUpstreamSystemdFactory(Etcd_3_2_Upstream_Systemd_Debian_9_Factory factory) {
        this.etcdUpstreamSystemd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        etcdDefaultsFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        etcdUpstreamSystemd.stopServices()
        debian.installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        etcdUpstreamSystemd.run()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
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
