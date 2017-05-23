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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8

import javax.inject.Inject

import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.service.external.Icinga

import groovy.util.logging.Slf4j

/**
 * Configures the Icinga 2 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Icinga_2_Debian_8 extends ScriptBase {

    @Inject
    Icinga_2_Debian_8_Properties debianPropertiesProvider

    Icinga_2_Upstream_Debian_8 upstream

    @Inject
    void setUpstreamFactory(Icinga_2_Upstream_Debian_8_Factory factory) {
        this.upstream = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        setupDefaults()
        upstream.run()
        updateGrub()
    }

    def setupDefaults() {
        log.info "Setup defaults for {}.", service
        Icinga service = service
        if (service.cgroups.size() == 0) {
            service.cgroups.addAll(defaultCgroups)
        }
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
        Icinga service = service
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
