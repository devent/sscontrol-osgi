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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * K8s-Master Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterDebian extends ScriptBase {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    K8sMasterUpstreamDebian upstream

    @Inject
    K8sMasterUfwDebianFactory ufwFactory

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setK8sMasterUpstreamDebianFactory(K8sMasterUpstreamDebianFactory factory) {
        this.upstream = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        debian.installPackages()
        debian.enableModules()
        upstream.setupDefaults()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstream.installKubeadm()
        upstream.createService()
        upstream.installKube()
        upstream.setupKubectl()
        upstream.waitNodeAvailable()
        upstream.postInstall()
        def joinCommand = upstream.joinCommand
        states << [kubeadmJoinCommand: joinCommand]
        upstream.installNetwork()
        upstream.waitNodeReady()
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
