/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.systemd.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master 1.5 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMaster_1_5_Systemd extends ScriptBase {

    def restartServices() {
        log.info 'Restarting k8s services.'
        [
            'kube-apiserver',
            'kube-controller-manager',
            'kube-scheduler'
        ].each {
            shell privileged: true, "service $it restart && service $it status" call()
        }
    }

    def configureServices() {
        log.info 'Configure k8s services.'
        K8sMaster service = service
        def dir = configDir
        def dest = "$dir/config"
        replace dest: "$dir/config", privileged: true with {
            [
                [
                    key: 'KUBE_LOG_LEVEL',
                    entry: { "--v=$it" },
                    value: service.debugLogging.modules['debug'].level
                ],
                [
                    key: 'KUBE_ALLOW_PRIV',
                    entry: { "--allow-privileged=$it" },
                    value: service.allowPrivileged
                ],
            ].each {
                log.info 'Replace entry {} in {}', it.entry, dest
                line "s/(?m)^#?${it.key}=\"--v=\\d*\".*/${it.key}=\"${it.entry(it.value)}\"/"
            }
            it
        }.call()
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
