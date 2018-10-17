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
class EtcdDebian extends ScriptBase {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Inject
    EtcdUpstreamDebianFactory upstreamFactory

    @Inject
    EtcdDefaultsFactory etcdDefaultsFactory

    @Inject
    EtcdVirtualInterfaceFactory virtualInterfaceFactory

    @Inject
    EtcdCheckFactory checkFactory

    EtcdUpstreamSystemdDebian etcdUpstreamSystemd

    DebianUtils debian

    ScriptBase etcdUfw

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setUpstreamSystemdFactory(EtcdUpstreamSystemdDebianFactory factory) {
        this.etcdUpstreamSystemd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setEtcdUfwFactory(EtcdUfwFactory factory) {
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
