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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12

import javax.inject.Inject

import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.docker.external.Docker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Docker</i> 1.12 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Docker_1_12_Debian_8 extends ScriptBase {

    @Inject
    Docker_1_12_Debian_8_Properties debianPropertiesProvider

    @Inject
    Docker_1_12_Systemd_Debian_8_Factory systemdFactory

    @Inject
    Docker_1_12_Upstream_Debian_8_Factory upstreamFactory

    @Override
    def run() {
        setupDefaults()
        installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        systemdFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        updateGrub()
    }

    def setupDefaults() {
        log.info "Setup defaults for {}.", service
        Docker service = service
        if (service.cgroups.size() == 0) {
            service.cgroups.addAll(defaultCgroups)
        }
    }

    def installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, timeout: aptgetTimeout, "apt-get -y install ${packages.join(' ')}" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    def updateGrub() {
        log.info "Update Grub for cgroups."
        def b = [
            cgroupsCmdLine,
            swapAccountCmdLine
        ]
        replace privileged: true, dest: grubConfigFile with {
            line "s/(?m)^GRUB_CMDLINE_LINUX=\".*\"/GRUB_CMDLINE_LINUX=\"${b.join(' ')}\"/"
            it
        }()
        shell privileged: true, "update-grub" call()
    }

    String getCgroupsCmdLine() {
        Docker service = service
        if (service.cgroups.size() == 0) {
            return ''
        } else {
            return new ST('cgroup_enable=<cgroups;separator=",">').add('cgroups', service.cgroups).render()
        }
    }

    String getSwapAccountCmdLine() {
        'swapaccount=1'
    }

    List getDefaultCgroups() {
        properties.getListProperty 'default_cgroups', defaultProperties
    }

    File getGrubConfigFile() {
        properties.getFileProperty 'grub_config_file', base, defaultProperties
    }

    Duration getAptgetTimeout() {
        properties.getDurationProperty 'apt_get_timeout', defaultProperties
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
