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

    def setupDefaults() {
        K8sMaster service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: defaultLogLevel
        }
        if (!service.allowPrivileged) {
            service.privileged defaultAllowPrivileged
        }
    }

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
                    key: 'KUBE_LOGTOSTDERR',
                    search: "--logtostderr=.*?",
                    replace: { "--logtostderr=$it" },
                    value: 'true'
                ],
                [
                    key: 'KUBE_LOG_LEVEL',
                    search: "--v=\\d+",
                    replace: { "--v=$it" },
                    value: service.debugLogging.modules['debug'].level
                ],
                [
                    key: 'KUBE_ALLOW_PRIV',
                    search: "--allow-privileged=.*?",
                    replace: { "--allow-privileged=$it" },
                    value: service.allowPrivileged
                ],
            ].each {
                log.info 'Replace `{}` -> `{}` in {}', it.search, it.replace(it.value), dest
                line "s/(?m)^#?${it.key}=\"${it.search}\".*/${it.key}=\"${it.replace(it.value)}\"/"
            }
            it
        }.call()
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    def getDefaultLogLevel() {
        properties.getNumberProperty('default_log_level', defaultProperties).intValue()
    }

    def getDefaultAllowPrivileged() {
        properties.getBooleanProperty 'default_allow_privileged', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
