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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7

import static com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7.K8sMasterDebianService.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.linux.k8s_1_7.AbstractK8sDockerLinux
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Docker.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterDockerDebian extends AbstractK8sDockerLinux {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    SystemdUtils systemd

    @Override
    def run() {
        super.run()
        reloadDaemon()
        stopServices()
        startServices()
        enableServices()
    }

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def reloadDaemon() {
        systemd.reloadDaemon()
    }

    def stopServices() {
        systemd.stopServices dockerServices
    }

    def startServices() {
        systemd.startServices dockerServices
    }

    def enableServices() {
        systemd.enableServices dockerServices
    }

    List getDockerServices() {
        properties.getListProperty 'docker_services', defaultProperties
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
