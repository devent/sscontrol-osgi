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
package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_letsencrypt_docker_systemd

import static org.apache.commons.io.FilenameUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory
import com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_letsencrypt_docker_script.ZimbraLetsEncryptDockerScript

import groovy.util.logging.Slf4j

/**
 * Uses docker to install certbot to create LetsEncrypt certificates for Zimbra.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class ZimbraLetsEncryptDockerSystemd extends ZimbraLetsEncryptDockerScript {

    SystemdUtils systemd

    @Inject
    void setSystemdFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    @Override
    def startServiceDocker() {
        systemd.startService 'docker'
    }

    @Override
    def stopDockerService() {
        systemd.stopService 'docker'
    }

    @Override
    def getLog() {
        log
    }
}
