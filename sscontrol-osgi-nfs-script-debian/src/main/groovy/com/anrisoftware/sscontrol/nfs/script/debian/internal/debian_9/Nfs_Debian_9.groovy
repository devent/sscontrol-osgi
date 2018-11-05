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
package com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9

import static com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9.Nfs_1_3_Debian_9_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.nfs.script.debian.external.Nfs_Debian
import com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9.Nfs_1_3_Debian_9_Factory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Nfs for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Nfs_Debian_9 extends Nfs_Debian {

    SystemdUtils systemd

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    def stopServices() {
        systemd.stopServices()
    }
    
    def startServices() {
        systemd.startServices()
    }
    
    def enableServices() {
        systemd.enableServices()
    }
    
    @Override
    def getLog() {
        log
    }

}