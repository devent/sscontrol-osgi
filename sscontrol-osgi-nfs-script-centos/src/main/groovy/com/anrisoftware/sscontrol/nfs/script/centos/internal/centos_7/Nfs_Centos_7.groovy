/*-
 * #%L
 * sscontrol-osgi - collectd-script-centos
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
package com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7

import static com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.Nfs_Centos_7_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.nfs.script.centos.external.Nfs_Centos
import com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.Nfs_1_3_Centos_7_Factory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd for CentOS 7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_Centos_7 extends Nfs_Centos {

    Nfs_Centos_7_Properties propertiesProvider

    Nfs_1_3_Centos_7 collectd

    SystemdUtils systemd

    @Inject
    void setCollectd_5_7_Centos_7_Factory(Nfs_1_3_Centos_7_Factory factory) {
        this.collectd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    @Override
    def run() {
        systemd.stopServices()
        installPackages()
        collectd.deployConfiguration()
        collectd.configureSELinux()
        systemd.startServices()
        systemd.enableServices()
    }

    String getCollectdService() {
        properties.getProperty 'collectd_service', defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
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
